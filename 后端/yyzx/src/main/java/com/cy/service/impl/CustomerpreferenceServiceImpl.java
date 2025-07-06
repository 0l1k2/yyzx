package com.cy.service.impl;

import com.cy.domain.Customerpreference;
import com.cy.dto.CustomerpreferenceDto;
import com.cy.mapper.CustomerpreferenceMapper;
import com.cy.service.ICustomerpreferenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import com.cy.vo.CountVo;
import com.cy.vo.CustomerpreferenceVo;
import org.springframework.beans.BeanUtils;
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
@Transactional(rollbackFor = Exception.class)
class CustomerpreferenceServiceImpl extends ServiceImpl<CustomerpreferenceMapper, Customerpreference> implements ICustomerpreferenceService {
    @Autowired
    private CustomerpreferenceMapper customerpreferenceMapper;

//    public ResultVo selectCustomerpreferencesVo(CustomerpreferenceDto customerpreferenceDto){
//        Map<String,Object> map = new HashMap<>();
//        customerpreferenceDto.setCurrentPage((customerpreferenceDto.getCurrentPage()-1)*customerpreferenceDto.getPageSize());
//        List<CustomerpreferenceVo> customerpreferenceVoList = customerpreferenceMapper.selectCustomerpreferencesVo(customerpreferenceDto);
//        CountVo countVo =customerpreferenceMapper.selectCustomerpreferencesCount(customerpreferenceDto);
//        map.put("total",countVo.getCount());
//        map.put("records",customerpreferenceVoList);
//        map.put("Size",customerpreferenceDto.getPageSize());
//        map.put("pages",Math.ceil((double)countVo.getCount()/customerpreferenceDto.getPageSize()));
//        return ResultVo.ok(map);
//
////        return new ResultVo(customerpreferenceMapper.selectCustomerpreferenceVo(customerpreferenceDto));
//    }
//}
public ResultVo selectCustomerpreferencesVo(CustomerpreferenceDto customerpreferenceDto) {
    // 1. 参数检查 - 确保dto不为null
    if (customerpreferenceDto == null) {
        customerpreferenceDto = new CustomerpreferenceDto();
    }

    // 2. 确保分页参数不为null
    Integer currentPage = customerpreferenceDto.getCurrentPage() != null ?
            customerpreferenceDto.getCurrentPage() : 1;
    Integer pageSize = customerpreferenceDto.getPageSize() != null ?
            customerpreferenceDto.getPageSize() : 10;

    // 3. 计算分页偏移量
    int offset = (currentPage - 1) * pageSize;

    // 4. 创建新的DTO对象用于查询，避免修改原始参数
    CustomerpreferenceDto queryDto = new CustomerpreferenceDto();
    BeanUtils.copyProperties(customerpreferenceDto, queryDto);
    queryDto.setCurrentPage(offset);

    // 5. 执行查询
    List<CustomerpreferenceVo> customerpreferenceVoList =
            customerpreferenceMapper.selectCustomerpreferencesVo(queryDto);

    // 6. 获取总数
    CountVo countVo = customerpreferenceMapper.selectCustomerpreferencesCount(queryDto);

    // 7. 确保countVo.getCount()不为null
    long totalCount = (countVo != null && countVo.getCount() != null) ?
            countVo.getCount() : 0;

    // 8. 构建响应
    Map<String, Object> map = new HashMap<>();
    map.put("total", totalCount);
    map.put("records", customerpreferenceVoList);
    map.put("Size", pageSize); // 使用安全处理的pageSize

    // 9. 安全计算总页数
    double pages = (pageSize > 0) ? Math.ceil((double) totalCount / pageSize) : 0;
    map.put("pages", pages);

    return ResultVo.ok(map);
}
}