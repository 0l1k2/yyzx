package com.cy.service;

import com.cy.domain.Customernurseitem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.CustomerNurseItemDto;
import com.cy.utils.ResultVo;

import java.util.List;


public interface ICustomernurseitemService extends IService<Customernurseitem> {
    ResultVo addItemToCustomer(List<Customernurseitem> customernurseitem);
    ResultVo removeCustomerLevelAndItem( Integer levelId,Integer custormerId);

    ResultVo listCustomerItem(CustomerNurseItemDto customernurseitemDto);
}
