package com.cy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cy.domain.Customernurseitem;
import com.cy.dto.CustomerNurseItemDto;
import com.cy.service.ICustomernurseitemService;
import com.cy.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customernurseitem")
@Tag(name="服务关注管理")
public class CustomernurseitemController {
    @Autowired
    private ICustomernurseitemService customernurseitemService;

    @Operation(summary="客户续费")
    @PostMapping("/enewNurseItem")
    public ResultVo enewNurseItem(@RequestBody Customernurseitem customernurseitem) {
     customernurseitemService.updateById(customernurseitem);
     return ResultVo.ok("续费成功");
    }

    @Operation(summary="判断客户是否已经配置了指定的某个项目")
    @GetMapping("/isIncludeItemCustormer/{custormerId}/{itemId}")
    public ResultVo isIncludeItemCustormer(@PathVariable Integer custormerId, @PathVariable Integer itemId) {
        LambdaQueryWrapper<Customernurseitem> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Customernurseitem::getCustomerId, custormerId).eq(Customernurseitem::getItemId, itemId);
        int count = customernurseitemService.count(lqw);
        if(count>0){
            return ResultVo.ok("该客户已经配置了该项目");
        }
        return ResultVo.ok("该客户不存在该项目");
    }

    @Operation(summary="移除客户护理项目")
    @GetMapping("/removeNurseItem/{id}")
    public ResultVo removeNurseItem(@PathVariable Integer id) {
        customernurseitemService.removeById(id);
        return ResultVo.ok("移除成功");
    }

    @Operation(summary="为客户单个或批量添加护理项目")
    @PostMapping("/addItemToCustomer")
    public ResultVo addItemToCustomer(@RequestBody List<Customernurseitem> customernurseitems) {
        return customernurseitemService.addItemToCustomer(customernurseitems);
    }

    @Operation(summary="为客户移除护理级别及其级联移除客户当前级别的护理项目")
    @GetMapping("/removeCustomerLevelAndItem/{levelId}/{customerId}")
    public ResultVo removeCustomerLevelAndItem(@PathVariable Integer levelId, @PathVariable Integer customerId) {
        return customernurseitemService.removeCustomerLevelAndItem(levelId,customerId);
    }

    @Operation(summary="获取客户护理项目列表-分页")
    @PostMapping("/listCustomerItem")
    public ResultVo listCustomerItem(@RequestBody CustomerNurseItemDto customernurseitemDto) {
        return customernurseitemService.listCustomerItem(customernurseitemDto);
    }

}

