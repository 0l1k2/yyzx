package com.cy.mapper;

import com.cy.domain.Beddetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.dto.BeddetailsDto;
import com.cy.vo.BedDetailsVo;
import com.cy.vo.CountVo;
import org.apache.ibatis.annotations.Delete;

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
     @Delete("delete from beddetails where id = #{id} and is_deleted = 0")
    Integer delBedDetails(Integer id);
 }
