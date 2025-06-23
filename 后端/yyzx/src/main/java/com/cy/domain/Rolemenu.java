package com.cy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2025-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Tag(name = "角色菜单关系表")
public class Rolemenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色编号
     */
    @Schema(description = "角色编号")
    private Integer role_id;

    /**
     * 菜单编号
     */
    @Schema(description = "菜单编号")
    private Integer menu;


}
