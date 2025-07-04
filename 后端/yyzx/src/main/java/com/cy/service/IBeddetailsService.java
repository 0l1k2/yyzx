package com.cy.service;

import com.cy.domain.Beddetails;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.dto.BeddetailsDto;
import com.cy.dto.ExchangeDto;
import com.cy.utils.ResultVo;
import com.cy.vo.BedDetailsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
public interface IBeddetailsService extends IService<Beddetails> {
ResultVo selectBedDetailsVo(BeddetailsDto bedDetailsDto);
ResultVo exchangeBed(ExchangeDto exchangeDto);

ResultVo delBedDetails(Integer id);
}
