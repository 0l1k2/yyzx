package com.cy.mapper;

import com.cy.domain.Customernurseitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.dto.CustomerNurseItemDto;
import com.cy.vo.CountVo;
import com.cy.vo.CustomerNurseItemVo;

import java.util.List;


public interface CustomernurseitemMapper extends BaseMapper<Customernurseitem> {
  List<CustomerNurseItemVo> listCustomerItem(CustomerNurseItemDto customerNurseItemDto);

  CountVo selectCustomerItemCount(CustomerNurseItemDto customerNurseItemDto);
}
