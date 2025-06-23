package com.cy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 * @version 1.0
 * @data 16:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="UserDto-用户查询条件", description = "")
public class UserDto {
    @Schema( description = "用户ID")
    private Integer roleId;
    @Schema( description = "当前页")
    private Integer currentPage;
    @Schema(description = "每页显示数量")
    private Integer pageSize;
    @Schema( description = "真实姓名")
    private String nickName;
}
