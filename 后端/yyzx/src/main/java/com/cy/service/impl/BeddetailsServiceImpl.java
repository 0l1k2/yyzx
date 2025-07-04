package com.cy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cy.domain.Bed;
import com.cy.domain.Beddetails;
import com.cy.domain.Customer;
import com.cy.dto.BeddetailsDto;
import com.cy.mapper.BedMapper;
import com.cy.mapper.BeddetailsMapper;
import com.cy.mapper.CustomerMapper;
import com.cy.service.IBedService;
import com.cy.service.IBeddetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import com.cy.vo.BedDetailsVo;
import com.cy.vo.CountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cy.dto.ExchangeDto;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
@Service
public class BeddetailsServiceImpl extends ServiceImpl<BeddetailsMapper, Beddetails> implements IBeddetailsService {
    @Autowired
    private BeddetailsMapper beddetailsMapper;

    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public ResultVo selectBedDetailsVo(BeddetailsDto beddetailsDto) {

        Map<String, Object> map = new HashMap<>();
        map.put("currentPage",beddetailsDto.getCurrentPage());
        beddetailsDto.setCurrentPage((beddetailsDto.getCurrentPage()-1)*beddetailsDto.getPageSize());
        List<BedDetailsVo> bedDetailsVoList=beddetailsMapper.selectBedDetailsVo(beddetailsDto);
        CountVo countVo=beddetailsMapper.selectCountVo(beddetailsDto);
        map.put("records",bedDetailsVoList);
        map.put("size",bedDetailsVoList.size());
        map.put("total",countVo.getCount());
//        map.put("pages",);
        map.put("pages",Math.ceil((double)countVo.getCount()/beddetailsDto.getPageSize()));
        return ResultVo.ok(map);
    }
    @Override
    public ResultVo exchangeBed(ExchangeDto exchangeDto){
        //查询床位是否使用
//        LambdaQueryWrapper<Bed> lqw=new LambdaQueryWrapper<>();
        Bed bed=bedMapper.selectById(exchangeDto.getNewBedId());
        System.out.println("************");
        System.out.println(exchangeDto);
        if(bed.getBedStatus()!=1){
            return ResultVo.fail("床位已被使用");
        }
        //修改客户旧床位详情信息 is_delete=1 床位使用结束时间为当前日期
        Beddetails beddetails=new Beddetails();
        beddetails.setId(exchangeDto.getId());
        beddetails.setEndDate(new Date());//结束时间为当前时间
        int count1=beddetailsMapper.updateById(beddetails);
        int row=beddetailsMapper.deleteById(beddetails.getId());
        //3.添加新床位详情信息
        Beddetails newbeddetails=new Beddetails();
        newbeddetails.setCustomerId(exchangeDto.getCustomerId());
        newbeddetails.setBedId(exchangeDto.getNewBedId());
        newbeddetails.setStartDate(new Date());//开始时间为当前时间
        newbeddetails.setEndDate(exchangeDto.getEndDate());//结束时间为空
        newbeddetails.setBedDetails(exchangeDto.getBuildingId()+"#"+exchangeDto.getNewRoomNo());
        int count2=beddetailsMapper.insert(newbeddetails);
        //4.修改旧床位状态为空闲 bed_status=1
        Bed oldBed=new Bed();
        oldBed.setId(exchangeDto.getOldBedId());
        oldBed.setBedStatus(1);
        int count3=bedMapper.updateById(oldBed);
        //5.修改新床位状态为有人 bed_status=2
        Bed newbed=new Bed();
        newbed.setId(exchangeDto.getNewBedId());
        newbed.setBedStatus(2);
        int count4=bedMapper.updateById(newbed);
        //6.修改客户信息 -- 新(房间/床位/楼)号
        Customer customer=new Customer();
        customer.setId(exchangeDto.getCustomerId());
        customer.setBedId(exchangeDto.getNewBedId());
        customer.setRoomNo(exchangeDto.getNewRoomNo());
        customer.setBuildingNo(exchangeDto.getBuildingId());
        int count5=customerMapper.updateById(customer);
        if (count1>0 && count2>0 && count3>0 && count4>0 && count5>0 && row>0){
            return ResultVo.ok("床位调换成功");
        }
        return ResultVo.fail("床位调换失败");
    }
    @Override
   public ResultVo delBedDetails(Integer id){
        beddetailsMapper.delBedDetails(id);
        return ResultVo.ok("删除成功");
    }
}
