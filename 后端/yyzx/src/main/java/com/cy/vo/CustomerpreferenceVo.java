package com.cy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author
 * @version 1.0
 * @data 15:16
 */

public class CustomerpreferenceVo {
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

    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "客户年龄")
    private Integer customerAge;
    @Schema(description = "客户性别")
    private String customerSex;
}
