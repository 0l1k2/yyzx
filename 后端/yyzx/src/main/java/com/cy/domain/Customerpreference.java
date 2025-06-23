package com.cy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author div F
 * @since 2025-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "顾客偏好")
public class Customerpreference implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 喜好ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "喜好ID")
    private Integer id;

    /**
     * 顾客ID
     */
    @Schema(description = "顾客ID")
    private Integer customerId;

    /**
     * 饮食喜好
     */
    @Schema(description = "饮食喜好")
    private String preferences;

    /**
     * 注意事项
     */
    @Schema(description = "注意事项")
    private String attention;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    @TableLogic
    @Schema(description = "是否删除")
    private Integer isDeleted;


}
