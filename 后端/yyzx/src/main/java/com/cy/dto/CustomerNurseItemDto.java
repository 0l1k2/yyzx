package com.cy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 * @version 1.0
 * @data 10:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "CustomerNurseItemDto对象")
public class CustomerNurseItemDto {
    @Schema(description = "当前页码")
    private Integer currentPage;
    @Schema(description = "每页显示数量")
    private Integer pageSize;

    @Schema(description = "客户编号")
    private Integer customerId;
    @Schema(description = "客户姓名")
    private String customerName;
//    @Schema(description = "客户性别")
//    private Integer manType;

}
