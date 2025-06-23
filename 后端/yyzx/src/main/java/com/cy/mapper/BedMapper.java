package com.cy.mapper;

import com.cy.domain.Bed;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.utils.ResultVo;
import com.cy.vo.ShowBedVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
public interface BedMapper extends BaseMapper<Bed> {
  ShowBedVo selectBedCount();
}
