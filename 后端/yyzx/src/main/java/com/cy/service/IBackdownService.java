package com.cy.service;

import com.cy.domain.Backdown;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.BackdownDto;
import com.cy.utils.ResultVo;


public interface IBackdownService extends IService<Backdown> {
    ResultVo examineBackdown(Backdown backdown);
    ResultVo lisBackdownVo(BackdownDto backdownDto);
}
