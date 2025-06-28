package com.cy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "NurseRecordsVo对象")
public class NurseRecordsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键")
    private Integer id;

    /**
     * 逻辑删除标记（0：显示；1：隐藏）
     */
    @TableLogic(value = "0", delval = "1")
    @Schema(description = "逻辑删除标记（0：显示；1：隐藏）")
    private Integer isDeleted;

    /**
     * 客户ID
     */
    @Schema(description = "客户ID")
    private Integer customerId;

    /**
     * 护理项目ID
     */
    @Schema(description = "护理项目ID")
    private Integer itemId;

    /**
     * 护理时间
     */
    @Schema(description = "护理时间")
    private Timestamp nursingTime;

    /**
     * 护理内容
     */
    @Schema(description = "护理内容")
    private String nursingContent;

    /**
     * 护理数量
     */
    @Schema(description = "护理数量")
    private Integer nursingCount;

    /**
     * 护理人员ID
     */
    @Schema(description = "护理人员ID")
    private Integer userId;

    /**
     * 护理人员的姓名
     * */
    @Schema(description = "护理人员的姓名")
    private String nickName;

    /**
     * 护理人员的手机号
     */
    @Schema(description = "护理人员的手机号")
    private String phoneNumber;

    /**
     *护理项目的编号
     */
    @Schema(description = "护理项目的编号")
    private String serialNumber;

    /**
     *护理项目的名称
     */
    @Schema(description = "护理项目的名称")
    private String NursingName;
}
