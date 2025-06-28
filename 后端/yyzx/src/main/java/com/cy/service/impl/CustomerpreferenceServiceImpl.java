package com.cy.service.impl;

import com.cy.domain.Customerpreference;
import com.cy.dto.CustomerpreferenceDto;
import com.cy.mapper.CustomerpreferenceMapper;
import com.cy.service.ICustomerpreferenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import com.cy.vo.CountVo;
import com.cy.vo.CustomerpreferenceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author div F
 * @since 2025-06-25
 */
@Service
@Transactional
class CustomerpreferenceServiceImpl extends ServiceImpl<CustomerpreferenceMapper, Customerpreference> implements ICustomerpreferenceService {
    @Autowired
    private CustomerpreferenceMapper customerpreferenceMapper;

    public ResultVo selectCustomerpreferencesVo(CustomerpreferenceDto customerpreferenceDto){
        Map<String,Object> map = new HashMap<>();
        customerpreferenceDto.setCurrentPage((customerpreferenceDto.getCurrentPage()-1)*customerpreferenceDto.getPageSize());
        List<CustomerpreferenceVo> customerpreferenceVoList = customerpreferenceMapper.selectCustomerpreferencesVo(customerpreferenceDto);
        CountVo countVo =customerpreferenceMapper.selectCustomerpreferencesCount(customerpreferenceDto);
        map.put("total",countVo.getCount());
        map.put("records",customerpreferenceVoList);
        map.put("Size",customerpreferenceDto.getPageSize());
        map.put("pages",Math.ceil((double)countVo.getCount()/customerpreferenceDto.getPageSize()));
        return ResultVo.ok(map);
        
//        return new ResultVo(customerpreferenceMapper.selectCustomerpreferenceVo(customerpreferenceDto));
    }

}
