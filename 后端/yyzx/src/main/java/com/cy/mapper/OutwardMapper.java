package com.cy.mapper;

import com.cy.domain.Outward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.dto.OutwardDto;
import com.cy.vo.OutwardVo;
import org.apache.ibatis.annotations.Param;
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

public interface OutwardMapper extends BaseMapper<Outward> {
//   List<OutwardVo> selectOutwardVo(@Param("userId") Integer userId,
//                                   @Param("currentPage") Integer currentPage,
//                                   @Param("pageSize") Integer pageSize);
//   @MapKey("id")
//   Map<String,Object> selectOutwardCount( @Param("userId") Integer userId);
//   Map<String,Object> selectOutwardCount(@Param("customerDto") OutwardDto outwardDto);
//  List<OutwardVo> selectOutwardVo(@Param("dto") OutwardDto dto);

   // 修改返回类型为OutWardCountVo，与Service层代码一致
//   OutWardCountVo selectOutwardCount(@Param("userId") Integer userId);
    List<OutwardVo> selectOutwardVo(
            OutwardDto outwardDto
    );
    CountVo selectOutwardCount(@Param("userId") Integer userId);


}
