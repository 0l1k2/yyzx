package com.cy.controller;


import com.cy.domain.Beddetails;
import com.cy.dto.BeddetailsDto;
import com.cy.dto.ExchangeDto;
import com.cy.service.IBeddetailsService;
import com.cy.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
@RestController
@RequestMapping("/beddetails")
@Tag(name = "床位信息管理")
public class BeddetailsController {
@Autowired
    private IBeddetailsService beddetailsService;

@Operation(summary = "查询所有床位信息列表-分页")
@PostMapping("/listBeddetails")
    public ResultVo listBeddetails(@RequestBody BeddetailsDto beddetailsDto){
       return  beddetailsService.selectBedDetailsVo(beddetailsDto);
        }

@Operation(summary = "更新床位使用详细-只能更新床位使用结束时间")
@PostMapping("/updateBeddetails")
    public ResultVo updateBeddetails(@RequestBody Beddetails beddetails) {
            return beddetailsService.updateById(beddetails)? ResultVo.ok("更新成功") : ResultVo.fail("更新失败");
        }

@Operation(summary = "删除床位使用详细")
@GetMapping("/deleteBeddetails/{id}")
    public ResultVo deleteBeddetails(@PathVariable Integer id) {
        return beddetailsService.removeById(id)? ResultVo.ok("删除成功") : ResultVo.fail("删除失败");
        }
@Operation(summary = "床位调换")
@PostMapping("/exchangeBed")
public ResultVo exchangeBed(@RequestBody ExchangeDto exchangeDto){
    return beddetailsService.exchangeBed(exchangeDto);
}
}

