package com.cy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.domain.Nursecontent;
import com.cy.dto.NurseItemDto;
import com.cy.service.INursecontentService;
import com.cy.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/nursecontent")
@Tag(name = "护理内容管理")
public class NursecontentController {
    @Autowired
    private INursecontentService nursecontentService;

    @Operation(summary = "添加护理项目")
    @PostMapping("/addNurseContent")
    public ResultVo addNurseContent(@RequestBody Nursecontent nursecontent) {
        return ResultVo.ok(nursecontentService.save(nursecontent),"护理项目添加成功");
    }


//    @Operation(summary = "查询护理项目-分页")
//    @PostMapping("/findNurseItemPage")
//    public ResultVo findNurseItemPage(@RequestBody NurseItemDto nurseItemDto) {
//        IPage<Nursecontent> page = new Page<>(nurseItemDto.getCurrentPage(),nurseItemDto.getPageSize());
//        LambdaQueryWrapper<Nursecontent> lqw = new LambdaQueryWrapper<>();
//        if(nurseItemDto.getItemName()!= null && nurseItemDto.getItemName().equals( "")){
//            lqw.like(Nursecontent::getNursingName,nurseItemDto.getItemName());
//        }
//        lqw.eq(Nursecontent::getStatus,nurseItemDto.getStatus());
//        nursecontentService.page(page,lqw);
//        return ResultVo.ok(page);
//    }
//@Operation(summary = "查询护理项目-分页")
//@PostMapping("/findNurseItemPage")
//public ResultVo findNurseItemPage(@RequestBody NurseItemDto nurseItemDto) {
//    IPage<Nursecontent> page = new Page<>(nurseItemDto.getCurrentPage(), nurseItemDto.getPageSize());
//    LambdaQueryWrapper<Nursecontent> lqw = new LambdaQueryWrapper<>();
//    // 修正护理名称查询条件
//    if (nurseItemDto.getItemName() != null && !nurseItemDto.getItemName().isEmpty()) {
//        lqw.like(Nursecontent::getNursingName, nurseItemDto.getItemName());
//    }
//    // 添加状态查询条件（确保状态值有效）
//    if (nurseItemDto.getStatus() != null) {
//        lqw.eq(Nursecontent::getStatus, nurseItemDto.getStatus());
//    }
//    // 添加排序（可选，但推荐）
//    lqw.orderByAsc(Nursecontent::getId); // 根据ID升序排列
//    nursecontentService.page(page, lqw);
//    return ResultVo.ok(page);
//}
//
@Operation(summary = "查询护理项目-分页")
@PostMapping("/findNurseItemPage")
public ResultVo findNurseItemPage(@RequestBody NurseItemDto nurseItemDto) {
    // 日志记录请求参数，便于调试
    System.out.println("查询护理项目分页参数: " + nurseItemDto);

    // 1. 处理分页参数，设置默认值避免空指针
    int currentPage = nurseItemDto.getCurrentPage() != null ? nurseItemDto.getCurrentPage() : 1;
    int pageSize = nurseItemDto.getPageSize() != null ? nurseItemDto.getPageSize() : 10;

    // 2. 确保分页参数有效性
    currentPage = Math.max(1, currentPage);
    pageSize = Math.max(1, Math.min(pageSize, 100)); // 限制每页最大数量

    IPage<Nursecontent> page = new Page<>(currentPage, pageSize);
    LambdaQueryWrapper<Nursecontent> lqw = new LambdaQueryWrapper<>();

    // 3. 优化条件查询逻辑，避免空指针
    if (nurseItemDto.getItemName() != null) {
        String itemName = nurseItemDto.getItemName().trim();
        if (!itemName.isEmpty()) {
            lqw.like(Nursecontent::getNursingName, itemName);
        }
    }

    // 4. 状态查询条件（确保状态值有效）
    if (nurseItemDto.getStatus() != null) {
        lqw.eq(Nursecontent::getStatus, nurseItemDto.getStatus());
    }

    // 5. 添加排序（推荐设置默认排序）
    lqw.orderByAsc(Nursecontent::getId);

    try {
        // 6. 执行分页查询
        nursecontentService.page(page, lqw);
        return ResultVo.ok(page, "查询成功");
    } catch (Exception e) {
        // 7. 异常处理，返回友好错误信息
        System.err.println("查询护理项目分页异常: " + e.getMessage());
        return ResultVo.fail("查询失败，请稍后重试");
    }
}


    @Operation(summary = "更新护理项目")
    @PostMapping("/updateNurseItem")
    public ResultVo updateNurseItem(@RequestBody Nursecontent nursecontent) {
        return nursecontentService.updateNurseItem(nursecontent);
    }
    @Operation(summary = "删除护理项目")
    @GetMapping("/delNurseItem/{id}")
    public ResultVo delNurseItem(@PathVariable Integer id){
        return nursecontentService.delNurseItem(id);
    }

}

