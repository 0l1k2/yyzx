package com.cy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 * @version 1.0
 * @data 9:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OutwardCountVo对象")
public class CountVo {
    @Schema(description = "总记录数")
    private Integer count;
}
