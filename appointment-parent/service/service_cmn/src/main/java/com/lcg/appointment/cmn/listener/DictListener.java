package com.lcg.appointment.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lcg.appointment.cmn.mapper.DictMapper;
import com.lcg.appointment.model.cmn.Dict;
import com.lcg.appointment.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;
    //构造函数
    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //一行一行读取
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
