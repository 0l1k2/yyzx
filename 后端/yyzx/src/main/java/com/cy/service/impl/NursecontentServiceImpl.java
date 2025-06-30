package com.cy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.domain.Nursecontent;
import com.cy.domain.Nurselevelitem;
import com.cy.mapper.NursecontentMapper;
import com.cy.mapper.NurselevelitemMapper;
import com.cy.service.INursecontentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class NursecontentServiceImpl extends ServiceImpl<NursecontentMapper, Nursecontent> implements INursecontentService {
    @Autowired
    private NursecontentMapper nursecontentMapper;
    @Autowired
    private NurselevelitemMapper nurselevelitemMapper;

    @Override
    public ResultVo updateNurseItem(Nursecontent nursecontent){
        //如果修改状态为停用，则删除关联的护理级别护理项目列表中对应的记录，来保证列表中的项目都是可用状态。
        if(nursecontent.getStatus() == 2){
            //查询当前护理项目是否在护理级别护理项目列表中，如果在则删除
            LambdaQueryWrapper<Nurselevelitem> lqw=new LambdaQueryWrapper();
            lqw.eq(Nurselevelitem::getItemId,nursecontent.getId());
            int count=nurselevelitemMapper.selectCount(lqw);
            if(count>0){
                LambdaQueryWrapper<Nurselevelitem> lqw2=new LambdaQueryWrapper();
                lqw2.eq(Nurselevelitem::getItemId,nursecontent.getId());
                int count2=nurselevelitemMapper.delete(lqw2);
                //更新护理项目
                int count3=nursecontentMapper.updateById(nursecontent);
                if(!(count2>0 && count3>0)){
                    return ResultVo.fail("更新失败");
                }
                return ResultVo.ok("更新成功");
            }
        }
        //更新护理项目
         nursecontentMapper.updateById(nursecontent);
        return ResultVo.ok("更新成功");
    }


    @Override
    public ResultVo delNurseItem(Integer id){
        //查询当前护理项目是否在护理等级护理项目列表中，如果在则删除
        LambdaQueryWrapper<Nurselevelitem> lqw=new LambdaQueryWrapper();
        lqw.eq(Nurselevelitem::getItemId,id);
        int count1=nurselevelitemMapper.selectCount(lqw);
        if(count1>0) {
             nurselevelitemMapper.delete(lqw);
             int count2=nursecontentMapper.deleteById(id);
             if(!(count2>0)){
                 return ResultVo.fail("删除失败");
             }
             return ResultVo.ok("删除成功");
        }
        nursecontentMapper.deleteById(id);
        return ResultVo.ok("删除成功");
        }

    @Override
    public ResultVo listNurseItemByLevel(Integer levelId){
         //查询别的项目配置，只查询item_id
//        LambdaQueryWrapper<Nurselevelitem> lqw=new LambdaQueryWrapper();
//        lqw.eq(Nurselevelitem::getLevelId,levelId).select(Nurselevelitem::getItemId);


//******************************itemIds**************************************//
//        QueryWrapper<Nurselevelitem> qw=new QueryWrapper<>();
//        qw.eq("level_id",levelId);
//        qw.select("item_id");
//        List<Object> itemIds=nurselevelitemMapper.selectObjs(qw);
//        List<Nursecontent> nursecontents=new ArrayList<>();
//        if (itemIds.size() > 0){
//            //2.查询护理项目信息
//            nursecontents=nursecontentMapper.selectBatchIds(itemIds);
//        }
//        return ResultVo.ok(nursecontents);
//        }
//********************************itemIds************************************//

    QueryWrapper<Nurselevelitem> qw = new QueryWrapper<>();
    qw.eq("level_id", levelId);
    qw.select("item_id");
    List<Object> itemIdsObj = nurselevelitemMapper.selectObjs(qw);

    List<Integer> itemIds = new ArrayList<>();
    for (Object obj : itemIdsObj) {
        if (obj instanceof Integer) {
            itemIds.add((Integer) obj);
        } else if (obj != null) {
            itemIds.add(Integer.parseInt(obj.toString()));
        }
    }

    List<Nursecontent> nursecontents = new ArrayList<>();
    if (!itemIds.isEmpty()) {
        // 2. 查询护理项目信息
        nursecontents = nursecontentMapper.selectBatchIds(itemIds);
    }
    return ResultVo.ok(nursecontents);
}

}
