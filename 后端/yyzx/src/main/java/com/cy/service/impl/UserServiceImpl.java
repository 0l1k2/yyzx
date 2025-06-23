package com.cy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.domain.Menu;
import com.cy.domain.Rolemenu;
import com.cy.domain.User;
import com.cy.mapper.MenuMapper;
import com.cy.mapper.RoleMapper;
import com.cy.mapper.RolemenuMapper;
import com.cy.mapper.UserMapper;
import com.cy.service.IRolemenuService;
import com.cy.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.utils.ResultVo;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static javafx.beans.binding.Bindings.select;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author div F
 * @since 2025-06-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
//    private UserMapper userMapper;
    private MenuMapper menuMapper;
    @Autowired//自动装配
    private RoleMapper roleMapper;
    @Autowired
    private RolemenuMapper rolemenuMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
   public ResultVo login(User user){
       // 1. 根据用户名查询用户信息
       LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
       lqw.eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword());
       User user1=getOne(lqw);
       //上面的or   User user1=userMapper.selectOne(lqw);
       System.out.println(user);
       if(user1!=null){
         //根据用户角色获取当前用户的菜单
           //1.获取得到角色对应的roleId
//           LambdaQueryWrapper<Rolemenu> listRolemenuLqw=new LambdaQueryWrapper<>();
//           listRolemenuLqw.eq(Rolemenu::getRole_id,user1.getRoleId());
//           listRolemenuLqw.select(Rolemenu::getMenu);
           QueryWrapper listRoleQw=new QueryWrapper();
           listRoleQw.eq("role_id",user1.getRoleId());
           listRoleQw.select("menu");
           List<Integer> menuIds=rolemenuMapper.selectObjs(listRoleQw);


           //2,根据menuIds获取一级菜单列表
           List<Menu> menus=menuMapper.selectBatchIds(menuIds);
           //3.查询子菜单
           for(Menu menu:menus){
               LambdaQueryWrapper<Menu> listmenuLqw=new LambdaQueryWrapper<>();
               listmenuLqw.eq(Menu::getParentId,menu.getId());
               menu.setChildren(menuMapper.selectList(listmenuLqw));
           }
           user.setMenuslist(menus);
           HashMap<String,Object> map=new HashMap<>();
           map.put("role_id",user1.getRoleId());
           // 如果登录验证成功，则需要生成令牌token，并返回给前端
            JwtBuilder builder= Jwts.builder();
            String token=builder.setSubject(user.getUsername())//主题，就是token中携带的数据
                    .setIssuedAt(new Date())//设置token创建时间
                    .setId(user1.getId().toString())//设置用户的ID为tokenID,唯一标识
                    .setClaims(map)//map中可以存放用户的角色权限信息
                    .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))//设置过期时间为24小时
                    .signWith(SignatureAlgorithm.HS256,"cy123456")//设置加密算法和密钥
                    .compact();
            return ResultVo.ok(user1,token);
       }
       return ResultVo.fail("用户名或密码错误");
   }
}
