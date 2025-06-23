package com.cy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 * @version 1.0
 * @data 16:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "dto对象")
public class OutwardDto {
    @Schema( description = "当前页")
    private Integer currentPage;
    @Schema(description = "每页显示数量")
    private Integer pageSize;
    @Schema(description = "用户Id")
    private Integer userId;
}
