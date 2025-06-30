package com.cy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.domain.Bed;
import com.cy.domain.Beddetails;
import com.cy.domain.Customer;
import com.cy.dto.KhxxDto;
import com.cy.mapper.BedMapper;
import com.cy.mapper.BeddetailsMapper;
import com.cy.mapper.CustomerMapper;
import com.cy.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import com.cy.vo.KhxxCustomerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// 在调用方法的类顶部添加
import com.cy.vo.KhxxCustomerVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author div F
 * @since 2025-06-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BedMapper   bedMapper;
    @Autowired
    private BeddetailsMapper beddetailsMapper;
   public ResultVo KhxxFindCustomer(KhxxDto khxxDto){
        List<KhxxCustomerVo> khxxCustomerVos = customerMapper.selectPageVo((khxxDto.getCurrentPage()-1)*khxxDto.getPageSize(), khxxDto.getPageSize(), khxxDto.getCustomerName(), khxxDto.getManType(), khxxDto.getUserId());
        Map<String,Object> map=new HashMap<>();
        map.put("total",khxxCustomerVos.size());
        map.put("records",khxxCustomerVos);
        map.put("size",khxxDto.getPageSize());
        map.put("current",khxxDto.getCurrentPage());
        map.put("pages",Math.ceil((double)khxxCustomerVos.size()/khxxDto.getPageSize()));
        return ResultVo.ok(map);
   }

//   public ResultVo addCustomer(Customer customer) throws Exception {
//       //查询床位是否可用
//       LambdaQueryWrapper<Bed> lqw=new LambdaQueryWrapper<>();
//       Bed bed=bedMapper.selectById(customer.getBedId());
//       if(bed.getBedStatus()!=1){
//           return ResultVo.fail("床位已被占用");
//       }customer.setUserId(-1);//默认新客户无健康管家
//       //生成客户信息
//
//       int count=customerMapper.insert(customer);
//
//       //生成入住详情信息
//       Beddetails beddetails=new Beddetails();
//       beddetails.setBedId(customer.getBedId());
//       beddetails.setStartDate(customer.getCheckinDate());
//       beddetails.setEndDate(customer.getExpirationDate());
//       beddetails.setBedDetails(customer.getBuildingNo()+"#"+bed.getBedNo());
//       beddetails.setCustomerId(customer.getId());
//       beddetails.setIsDeleted(0);//床位生效
//      int count2= beddetailsMapper.insert(beddetails);
//
//      //修改床位状态
//      Bed bedUpdate=new Bed();
////      bedUpdate.setId(customer.getId());
//       bedUpdate.setId(bed.getId());
//      bedUpdate.setBedStatus(2);
//      int count3=bedMapper.updateById(bedUpdate);
//      if(!(count>0&&count2>0&&count3>0)){
//          throw new Exception("入住失败!!!");
//      }
//      return ResultVo.ok("入住成功");
//   }
public ResultVo addCustomer(Customer customer) throws Exception {
    // 1. 参数基础检查
    if (customer == null) {
        throw new Exception("客户信息不能为空");
    }

    if (customer.getBedId() == null) {
        throw new Exception("床位ID不能为空");
    }

    // 2. 床位查询与空值检查
    Bed bed = bedMapper.selectById(customer.getBedId());
    if (bed == null) { // 添加空值检查
        return ResultVo.fail("床位不存在");
    }

    if (bed.getBedStatus() != 1) {
        return ResultVo.fail("床位已被占用");
    }

    // 3. 设置默认值
    customer.setUserId(-1); // 默认新客户无健康管家

    // 4. 插入客户信息
    int count = customerMapper.insert(customer);
    if (count <= 0) {
        throw new Exception("客户信息保存失败");
    }

    // 5. 创建入住详情
    Beddetails beddetails = new Beddetails();
    beddetails.setBedId(customer.getBedId());
    beddetails.setStartDate(customer.getCheckinDate());
    beddetails.setEndDate(customer.getExpirationDate());

    // 安全拼接字符串
    String buildingNo = customer.getBuildingNo() != null ? customer.getBuildingNo() : "";
    String bedNo = bed.getBedNo() != null ? bed.getBedNo() : "";
    beddetails.setBedDetails(buildingNo + "#" + bedNo);

    beddetails.setCustomerId(customer.getId());
    beddetails.setIsDeleted(0);

    int count2 = beddetailsMapper.insert(beddetails);
    if (count2 <= 0) {
        throw new Exception("入住详情保存失败");
    }

    // 6. 更新床位状态
    Bed bedUpdate = new Bed();
    bedUpdate.setId(bed.getId()); // 使用查询到的床位ID
    bedUpdate.setBedStatus(2);

    int count3 = bedMapper.updateById(bedUpdate);
    if (count3 <= 0) {
        throw new Exception("床位状态更新失败");
    }

    return ResultVo.ok("入住成功");
}
   @Override
    public ResultVo editCustomer(Customer customer) throws Exception {
       int count=customerMapper.updateById(customer);
       //如果合同时间到期发生改变则需要更新当前用户生成的床位信息的退住时间为改变后的合同时间
       if(customer.getExpirationDate()!=null){
            LambdaUpdateWrapper<Beddetails> luw=new LambdaUpdateWrapper<>();
            luw.eq(Beddetails::getCustomerId,customer.getId())
                    .set(Beddetails::getEndDate,customer.getExpirationDate());
            Beddetails beddetails=new Beddetails();
            beddetails.setEndDate(customer.getExpirationDate());
            int count2=beddetailsMapper.update(beddetails,luw);
           if (!(count2>0&&count>0)){
               throw new Exception("修改失败");
           }
       }
       return ResultVo.ok("修改成功");
   }
   @Override
    public ResultVo removeCustomer(Integer id, Integer bedId) throws Exception {
       int count1=customerMapper.deleteById(id);
       //床位状态改为空闲
       LambdaUpdateWrapper<Bed> luw=new LambdaUpdateWrapper<>();
       luw.eq(Bed::getId,bedId)
              .set(Bed::getBedStatus,1);
       int count2=bedMapper.update(null,luw);
       //床位详情状态改为隐藏 删除床位详情信息
       LambdaQueryWrapper<Beddetails> lqw=new LambdaQueryWrapper<>();
       lqw.eq(Beddetails::getCustomerId,id)
              .eq(Beddetails::getBedId,bedId);
       int count3=beddetailsMapper.delete(lqw);
       if(!(count1>0&&count2>0&&count3>0)){
           throw new Exception("删除失败");
       }
       return  ResultVo.ok("删除成功");
   }




}
