package com.cy.service;

import com.cy.domain.Customerpreference;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.CustomerpreferenceDto;
import com.cy.mapper.CustomerpreferenceMapper;
import com.cy.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author div F
 * @since 2025-06-25
 */
@Service
public interface ICustomerpreferenceService extends IService<Customerpreference> {

ResultVo selectCustomerpreferencesVo(CustomerpreferenceDto customerpreferenceDto);


}
