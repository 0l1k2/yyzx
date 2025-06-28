package com.cy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author
 * @version 1.0
 * @data 14:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="ExchangeDto对象")
public class ExchangeDto {
    @Schema(description="床位详情编号")
    private Integer id;

    @Schema(description="客户编号")
    private Integer customerId;

    @Schema(description="新楼号")
    private String BuildingId;

    @Schema(description="新房间号")
    private String newRoomNo;

    @Schema(description="新床位号")
    private Integer newBedId;

    @Schema(description="旧床位号")
    private Integer oldBedId;

    @Schema(description="结束日期")
    private Date endDate;

}
