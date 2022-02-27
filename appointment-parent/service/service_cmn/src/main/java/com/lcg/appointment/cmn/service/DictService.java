package com.lcg.appointment.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcg.appointment.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChildData(Long id);
    //导出数据
    void exportData(HttpServletResponse response);

    //导入数据
    void importDictData(MultipartFile file);

    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
