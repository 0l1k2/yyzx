package com.cy.mapper;

import com.cy.domain.Beddetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.dto.BeddetailsDto;
import com.cy.vo.BedDetailsVo;
import com.cy.vo.CountVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */

public interface BeddetailsMapper extends BaseMapper<Beddetails> {
     List<BedDetailsVo> selectBedDetailsVo(BeddetailsDto bedDetailsDto);
     CountVo selectCountVo(BeddetailsDto bedDetailsDto);
 }
