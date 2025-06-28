package com.cy.service;

import com.cy.domain.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.KhxxDto;
import com.cy.utils.ResultVo;
import com.cy.vo.KhxxCustomerVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
public interface ICustomerService extends IService<Customer> {
    ResultVo KhxxFindCustomer(KhxxDto khxxDto);

    ResultVo addCustomer(Customer customer) throws Exception;
    ResultVo editCustomer(Customer customer) throws Exception;

    ResultVo removeCustomer(Integer id,Integer bedId) throws Exception;

}
