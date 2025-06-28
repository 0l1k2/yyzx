package com.cy.controller;


import com.cy.service.IRoomService;
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
 * @since 2025-06-26
 */
@RestController
@RequestMapping("/room")
@Tag(name = "房间管理")
public class RoomController {
    @Autowired
    private IRoomService roomService;

  @Operation(summary = "查询房间列表")
  @GetMapping("/listRoom")
    public ResultVo listRoom() {
        return ResultVo.ok(roomService.list());
    }

  @Operation(summary="床位示意图")
    @GetMapping("/findShowBedVo/{floor}")
    public ResultVo findShowBedVo(@PathVariable String floor) {
        return ResultVo.ok(roomService.findShowBedVo(floor));
  }



}

