package com.cy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.domain.Food;
import com.cy.service.IFoodService;
import com.cy.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author div F
 * @since 2025-06-25
 */
@RestController
@RequestMapping("/food")
@Tag(name = "食品管理")
public class FoodController {
    @Autowired
    private IFoodService foodService;

   @Operation(summary = "查询所有食品")
    @GetMapping("/listFood/{currentPage}/{pageSize}")
    public ResultVo listFood(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize){
       IPage<Food> page =new Page<>(currentPage,pageSize);
       return ResultVo.ok(foodService.page(page));
   }
}

