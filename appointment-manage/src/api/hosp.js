import request from '@/utils/request'

export default{
    //医院列表
    getHospList(page,limit,searchObj){
        return request ({
            url: `/admin/hosp/hospital/list/${page}/${limit}`,
            method: 'get',
            params: searchObj //get请求不能使用json形式因此用params：对象名称;
          })
    },
    //根据dictcode查询所有子节点(所有省)
    findByDictCode(dictCode){
        return request({
            url:`/admin/cmn/dict/findByDictCode/${dictCode}`,
            method:'get'
        })
    },
    //根据省查询出下面的所有市
    findChildId(id){
        return request({
            url: `/admin/cmn/dict/findChildData/${id}`,
            method: 'get'
        })
    },
    updateStatus(id, status) {
        return request({
          url: `/admin/hosp/hospital/updateStatus/${id}/${status}`,
          method: 'get'
        })
    },
    //查看医院详情
    getHospById(id) {
        return request ({
        url: `/admin/hosp/hospital/showHospDetail/${id}`,
        method: 'get'
        })
    },
    //查询医院科室
    getDeptByHoscode(hoscode) {
        return request ({
        url: `/admin/hosp/department/getDepList/${hoscode}`,
        method: 'get'
        })
    },
    //查询预约规则
    getScheduleRule(page,limit,hoscode,depcode) {
        return request ({
        url: `/admin/hosp/schedule/getScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
        method: 'get'
        })
    },
    //查询排班详情
    getScheduleDetail(hoscode,depcode,workDate) {
        return request ({
        url: `/admin/hosp/schedule/getScheduleDetail/${hoscode}/${depcode}/${workDate}`,
        method: 'get'
        })
    }
  
}