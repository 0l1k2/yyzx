package com.cy.service;

import com.cy.domain.Nursecontent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.utils.ResultVo;


public interface INursecontentService extends IService<Nursecontent> {
    ResultVo updateNurseItem(Nursecontent nursecontent);
    ResultVo delNurseItem(Integer id);

    ResultVo listNurseItemByLevel(Integer levelId);
}
