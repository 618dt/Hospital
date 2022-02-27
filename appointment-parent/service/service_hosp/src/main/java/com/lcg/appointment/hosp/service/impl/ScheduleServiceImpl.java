package com.lcg.appointment.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcg.appointment.common.exception.HospitalException;
import com.lcg.appointment.common.result.ResultCodeEnum;
import com.lcg.appointment.common.utils.DateTimeUtil;
import com.lcg.appointment.hosp.mapper.ScheduleMapper;
import com.lcg.appointment.hosp.repository.ScheduleRepository;
import com.lcg.appointment.hosp.service.DepartmentService;
import com.lcg.appointment.hosp.service.HospitalService;
import com.lcg.appointment.hosp.service.ScheduleService;
import com.lcg.appointment.model.hosp.BookingRule;
import com.lcg.appointment.model.hosp.Department;
import com.lcg.appointment.model.hosp.Hospital;
import com.lcg.appointment.model.hosp.Schedule;
import com.lcg.appointment.vo.hosp.BookingScheduleRuleVo;
import com.lcg.appointment.vo.hosp.ScheduleOrderVo;
import com.lcg.appointment.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper,Schedule> implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;
    //上传排班接口
    @Override
    public void save(Map<String, Object> paramMap) {
        //将参数转换为department对象
        String paramString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramString, Schedule.class);
        //根据医院编号和科室编号查询科室数据,判断科室是否存在
        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        //判断
        if (scheduleExist != null) {//不为空修改
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(schedule.getStatus());
            scheduleRepository.save(scheduleExist);
        }else{//为空上传数据
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }
    }

    //查询排班接口
    @Override
    public Page<Schedule> findPageSchedule(int curPage, int limit, ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable = PageRequest.of(curPage, limit);
        Schedule schedule = new Schedule();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    //删除排班接口
    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    //根据 医院编号 和 科室编号 查询排班规则数据
    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode) {
        //1.根据hoscode和depcode查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        //2.根据工作日期(workDate)分组,聚合操作使用Aggregation
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配条件
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")//first:从哪个值开始,as:别名
                        //3.统计号源数量
                        .count().as("docCount")//就诊的医生数量
                        .sum("reservedNumber").as("reservedNumber")//可预约数
                        .sum("availableNumber").as("availableNumber"),//科室剩余预约数
                //排序
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                //4.实现分页
                Aggregation.skip((page - 1) * limit),//跳过多少记录
                Aggregation.limit(limit)//每页显示多少记录
        );
        //调用方法,最终执行(agg聚合,查询的类,返回的类),返回类使用封装好的VO类
        AggregationResults<BookingScheduleRuleVo> aggResult = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        //得到数据集合
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResult.getMappedResults();

        //得到分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalResults = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalResults.getMappedResults().size();//得到记录数

        //根据日期获取到对应的星期
        for (BookingScheduleRuleVo b : bookingScheduleRuleVoList){
            Date workDate = b.getWorkDate();
            String dayOfWeek = DateTimeUtil.getDayOfWeek(new DateTime(workDate));
            b.setDayOfWeek(dayOfWeek);
        }

        //根据医院编号获取医院名称
        String hosName = hospitalService.getByHoscode(hoscode).getHosname();

        //设置最终数据,进行返回
        Map<String,Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);
        //为了结构更加清晰,重新创建一个map存放医院名称
        Map<String,Object> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);
        return result;
    }

    //根据 医院编号 和 科室编号 和工作日期 查询排班详细信息
    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        //根据spring data规范构造repository的查询函数
        List<Schedule> list = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode,new DateTime(workDate).toDate());
        //把得到的list集合遍历,设置其他值:医院名称、科室名称、日期对应星期
        list.stream().forEach(schedule->{
            //调用封装方法
            this.packageSchedule(schedule);
        });
        return list;
    }

    //获取可预约的排班数据
    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        //存放返回结果
        Map<String,Object> result = new HashMap<>();
        //获取预约规则
        //根据医院编号获取医院对象
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if (hospital == null) {
            throw new HospitalException(ResultCodeEnum.DATA_ERROR);
        }
        //从医院对象中获取预约规则
        BookingRule bookingRule = hospital.getBookingRule();
        //获取可预约日期的分页数据
        IPage iPage = this.getListDate(page, limit, bookingRule);
        //获取可预约日期,将日期做分页显示
        List<Date> dateList = iPage.getRecords();

        //获取可预约日期里面科室的剩余预约数,查询mongoDB
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode)
                .and("workDate").in(dateList);
        //聚合查询:统计,求和; .as取别名
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregateResult =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        //得到可预约排班规则集合
        List<BookingScheduleRuleVo> scheduleVoList = aggregateResult.getMappedResults();

        //合并数据  map集合 key日期  value预约规则和剩余数量等,方便后续处理
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(
                            Collectors.toMap(BookingScheduleRuleVo::getWorkDate,
                                    BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }

        //获取可预约排班规则
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for(int i=0,len=dateList.size();i<len;i++) {//遍历可预约日期
            Date date = dateList.get(i);
            //从map集合根据key日期获取value值
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            //如果当天没有排班医生
            if(bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
                //科室剩余预约数  -1表示无号
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //计算当前预约日期对应星期
            String dayOfWeek = DateTimeUtil.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if(i == len-1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //当天预约如果过了停号时间,不能预约
            if(i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()) {
                    //停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //可预约日期规则数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());

        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.getByHoscode(hoscode).getHosname());
        //科室
        Department department =departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
        //月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        //放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }

    //根据排班id查询排班信息
    @Override
    public Schedule getScheduleId(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        //需要将查询的排班信息封装其他值
        return this.packageSchedule(schedule);
    }

    //根据排班id获取下单数据
    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //获取排班信息
        Schedule schedule = this.getScheduleId(scheduleId);
        if (schedule == null) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        //获取预约规则信息
        Hospital hospital = hospitalService.getByHoscode(schedule.getHoscode());
        if (hospital == null) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if (bookingRule == null) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        //将获取的数据设置到scheduleOrderVo中
        scheduleOrderVo.setHoscode(schedule.getHoscode());
        String hosname = hospitalService.getByHoscode(schedule.getHoscode()).getHosname();
        scheduleOrderVo.setHosname(hosname);
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        String depname = departmentService.getDepartment(schedule.getHoscode(), schedule.getDepcode()).getDepname();
        scheduleOrderVo.setDepname(depname);
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());
        //退号截止天数（如：就诊前一天为-1，当天为0）
        int quitDay = bookingRule.getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //预约开始时间
        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //预约截止时间
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        //当天停止挂号时间
        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());
        return scheduleOrderVo;
    }

    //更新排班信息,用于mq操作
    @Override
    public void update(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        scheduleRepository.save(schedule);
    }

    //封装排班详情的其他值
    private Schedule packageSchedule(Schedule schedule) {
        //设置医院名称
        String hosname = hospitalService.getByHoscode(schedule.getHoscode()).getHosname();
        schedule.getParam().put("hosname",hosname);
        //设置科室名称
        String depname = departmentService.getDepartment(schedule.getHoscode(), schedule.getDepcode()).getDepname();
        schedule.getParam().put("depname", depname);
        //设置日期对应星期
        String dayOfWeek = DateTimeUtil.getDayOfWeek(new DateTime(schedule.getWorkDate()));
        schedule.getParam().put("dayOfWeek",dayOfWeek);
        return schedule;
    }

    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     * 即将日期和时间做一个拼接
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " "+ timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    private IPage getListDate(Integer page, Integer limit, BookingRule bookingRule) {
        //获取当天放号时间 年 月 日 时 分
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //获取预约周期
        Integer cycle = bookingRule.getCycle();
        //如果当天放号时间已经超过了当前时间,预约周期从后一天开始计算,周期+1
        if (releaseTime.isBeforeNow()){
            cycle += 1;
        }
        //获取可预约的所有日期,最后一天显示即将放号
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            //当前日期,从0~5分别相加即得5天的周期内的可预约日期
            DateTime curDateTime = new DateTime().plusDays(i);
            //将日期转为年月日格式
            String dateString = curDateTime.toString("yyyy-MM-dd");
            //将得到的日期追加到前面创建的list集合中
            dateList.add(new DateTime(dateString).toDate());
        }
        //因为每个医院预约周期可能不同,规定每页显示日期最多7天数据
        List<Date> pageDateList = new ArrayList<>();
        int start = (page-1)*limit;//当前页数据显示的开始位置=(当前页-1)*每页记录数
        int end = start+limit;//当前页数据显示的末尾位置
        //当可预约日期总记录数没有达到理论上的末尾位置
        if (dateList.size() < end) {
            end = dateList.size();//需要将理论的末尾位置改为实际的;
        }
        //将dateList的数据放入分页日期列表中,即提取出当前页中的日期数据
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }

        //分页(当前页号,每页记录数,总记录数)
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page,7,dateList.size());
        iPage.setRecords(pageDateList);//?dateList
        return iPage;
    }

}
