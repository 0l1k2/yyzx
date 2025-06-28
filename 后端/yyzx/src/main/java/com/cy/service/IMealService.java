package com.cy.service;

import com.cy.domain.Meal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.MealDto;
import com.cy.utils.ResultVo;
import com.cy.vo.MealVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author div F
 * @since 2025-06-25
 */
public interface IMealService extends IService<Meal> {
   ResultVo listMealPage(MealDto mealDto);

}
