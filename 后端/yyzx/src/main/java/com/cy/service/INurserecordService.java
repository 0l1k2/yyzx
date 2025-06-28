package com.cy.service;

import com.cy.domain.Nurserecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.NurseRecordDto;
import com.cy.utils.ResultVo;


public interface INurserecordService extends IService<Nurserecord> {
    ResultVo addNurseRecord(Nurserecord nurserecord);
    ResultVo listNurseRecordsVo(NurseRecordDto nurseRecordDto);
}
