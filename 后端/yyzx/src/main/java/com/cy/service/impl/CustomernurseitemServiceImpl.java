package com.cy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cy.domain.Customer;
import com.cy.domain.Customernurseitem;
import com.cy.dto.CustomerNurseItemDto;
import com.cy.mapper.CustomerMapper;
import com.cy.mapper.CustomernurseitemMapper;
import com.cy.service.ICustomernurseitemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import com.cy.vo.CountVo;
import com.cy.vo.CustomerNurseItemVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class CustomernurseitemServiceImpl extends ServiceImpl<CustomernurseitemMapper, Customernurseitem> implements ICustomernurseitemService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomernurseitemMapper customernurseitemMapper;

    @Override
   public ResultVo addItemToCustomer(List<Customernurseitem> customernurseitems){
       if(customernurseitems.size()>0) {
           //判断是否选择级别添加级别中的护理项目还是单独购买护理项目
           if (customernurseitems.get(0).getLevelId() != null) {//级别附带项目
               //设置客户护理级别
               Customer customer = new Customer();
               customer.setId(customernurseitems.get(0).getCustomerId());
               customer.setLevelId(customernurseitems.get(0).getLevelId());
               int count1 = customerMapper.updateById(customer);
               //批量给用户添加护理项目
               boolean temp = saveBatch(customernurseitems);
               if (!(count1 > 0 && temp)) {
                   return ResultVo.fail("添加护理项目失败");
               }
           } else {
               //单独购买项目
               saveBatch(customernurseitems);
           }
          return ResultVo.ok("添加护理项目成功");
       }
       return ResultVo.fail("请选择需要添加的护理项目");
   }

   @Override
    public ResultVo removeCustomerLevelAndItem(Integer levelId, Integer  custormerId){
       //更新客户级别为空
       LambdaUpdateWrapper<Customer> luw = new LambdaUpdateWrapper<>();
       luw.eq(Customer::getId, custormerId).set(Customer::getLevelId, null);
       int count1 = customerMapper.update(null, luw);
       //删除客户护理项目
       LambdaUpdateWrapper<Customernurseitem> luw2 = new LambdaUpdateWrapper<>();
       luw2.eq(Customernurseitem::getCustomerId, custormerId)
               .eq(Customernurseitem::getLevelId, levelId);
       int count2 = customernurseitemMapper.delete(luw2);
       if(!(count1>0 && count2>0)){
           return ResultVo.fail("护理项目移除失败");
       }
       return ResultVo.ok("护理项目移除成功");
    }

//    public ResultVo listCustomerItem(CustomerNurseItemDto customerNurseItemDto) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("currentPage",customerNurseItemDto.getCurrentPage());
//        List<CustomerNurseItemVo> customerNurseItemVoList=customernurseitemMapper.listCustomerItem(customerNurseItemDto);
//        CountVo countVo = customernurseitemMapper.selectCustomerItemCount(customerNurseItemDto);
//        map.put("records",customerNurseItemVoList);
//        map.put("total",countVo.getCount());
//        map.put("size",customerNurseItemDto.getPageSize());
//        map.put("pages",Math.ceil((double)countVo.getCount()/customerNurseItemDto.getPageSize()));
//        return ResultVo.ok(map);
//    }
@Override
public ResultVo listCustomerItem(CustomerNurseItemDto customerNurseItemDto) {
    // 打印原始接收参数
    System.out.println("接收参数：" + customerNurseItemDto.toString());

    // 处理分页参数
    Integer currentPage = customerNurseItemDto.getCurrentPage();
    Integer pageSize = customerNurseItemDto.getPageSize();

    // 设置默认值
    if (currentPage == null) {
        currentPage = 1;
    }
    if (pageSize == null) {
        pageSize = 10;
    }

    // 计算正确的SQL LIMIT偏移量
    Integer offset = (currentPage - 1) * pageSize;

    // 将计算结果设置回原始DTO - 关键修改点
    customerNurseItemDto.setCurrentPage(offset);
    customerNurseItemDto.setPageSize(pageSize);

    // 执行数据库查询 - 使用原始DTO作为参数
    List<CustomerNurseItemVo> customerNurseItemVoList = customernurseitemMapper.listCustomerItem(customerNurseItemDto);
    CountVo countVo = customernurseitemMapper.selectCustomerItemCount(customerNurseItemDto);

    // 构建返回结果
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("records", customerNurseItemVoList);
    resultMap.put("total", countVo.getCount());
    resultMap.put("size", pageSize);
    resultMap.put("current", currentPage); // 注意：这里使用原始currentPage，而非offset
    resultMap.put("pages", Math.ceil((double) countVo.getCount() / pageSize));

    return ResultVo.ok(resultMap);
}

}
