package com.cy.mapper;

import com.cy.domain.Customerpreference;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.dto.CustomerpreferenceDto;
import com.cy.vo.CountVo;
import com.cy.vo.CustomerpreferenceVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author div F
 * @since 2025-06-25
 */
public interface CustomerpreferenceMapper extends BaseMapper<Customerpreference> {
    List<CustomerpreferenceVo> selectCustomerpreferencesVo(CustomerpreferenceDto customerpreferenceDto);
    CountVo selectCustomerpreferencesCount(CustomerpreferenceDto customerpreferenceDto);
}
