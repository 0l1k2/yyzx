package com.cy.service;

import com.cy.domain.Outward;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.OutwardDto;
import com.cy.utils.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
public interface IOutwardService extends IService<Outward> {
   ResultVo selectOutwardVo(OutwardDto outwardDto);
   ResultVo examineOutward(Outward outwardDto);
}
