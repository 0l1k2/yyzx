package com.cy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cy.domain.Bed;
import com.cy.service.IBedService;
import com.cy.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
@RestController
@RequestMapping("/bed")
@Tag(name = "床位管理")
public class BedController {
  @Autowired
    private IBedService bedService;

    @PostMapping("/findBed")
    @Operation(summary = "查询床位信息")
    public ResultVo findBed(@RequestBody Bed bed) {
        LambdaQueryWrapper<Bed> lqw = new LambdaQueryWrapper<>();
        if (bed.getRoomNo()!= null){
            lqw.eq(Bed::getRoomNo, bed.getRoomNo());
        }
        if (bed.getBedStatus()!= null){
            lqw.eq(Bed::getBedStatus, bed.getBedStatus());
        }
        return ResultVo.ok(bedService.list(lqw));

    }
}

