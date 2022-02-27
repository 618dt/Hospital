package com.lcg.appointment.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcg.appointment.cmn.listener.DictListener;
import com.lcg.appointment.cmn.mapper.DictMapper;
import com.lcg.appointment.cmn.service.DictService;
import com.lcg.appointment.model.cmn.Dict;
import com.lcg.appointment.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    //根据数据id查询子数据列表
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //向list集合每个dict对象中设置hasChildren
        for (Dict dict:dictList) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }
    //判断id下面是否有子节点,即将此id当作parent_id进行查询
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        // 0>0    1>0
        return count>0;
    }

    //导出数据字典
    @Override
    public void exportData(HttpServletResponse response) {
        /*设置下载信息 */
        //设置类型为excel
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据字典", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置头信息,以下载方式打开
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        /*查询数据库,查询出所有数据字典*/
        List<Dict> dictList = baseMapper.selectList(null);
        /*将查询到的Dict转为DictEeVo*/
        //定义一个和dictList一样长度的列表
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
        //遍历转换每一个对象
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            //dictEeVo.setId(dict.getId()); 被封装再下一行代码的方法中
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVoList.add(dictEeVo);
        }

        /*调用方法进行写操作*/
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据字典
     * allEntries = true: 方法调用后清空所有缓存
     * @param file
     */
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        //输入流,对象,监听器
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据dictCode和value查询
    @Override
    public String getDictName(String dictCode, String value) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty(dictCode)) {
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else{
            //注意是getId
            Long parentId = this.getDictByDictCode(dictCode).getId();
            wrapper.eq("value", value);
            wrapper.eq("parent_id", parentId);
            //根据value和parentId查询;
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }
    }


    @Override
    @Cacheable(value = "dictCode", keyGenerator = "keyGenerator")
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取到id
        Long id = this.getDictByDictCode(dictCode).getId();
        //根据id获取到下属子节点
        List<Dict> dictList = this.findChildData(id);
        return dictList;
    }

    //根据dictCode查询到parentId;
    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }

}
