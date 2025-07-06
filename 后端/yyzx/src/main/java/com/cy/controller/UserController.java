package com.cy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.domain.User;
import com.cy.dto.UserDto;
import com.cy.service.IUserService;
import com.cy.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author div F
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResultVo login(@RequestBody User user) {
        return userService.login(user);
    }

    //查询用户
    @Operation(summary = "查询系统用户-分页")
    @PostMapping("/findUserPage")
    public ResultVo findUserPage(@RequestBody UserDto userDto) {
//        Page<User> page = new Page<>(userDto.getCurrentPage(), userDto.getPageSize());
//        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
//        if(userDto.getNickName()!=null&& userDto.getNickName()!=("")){
//            lqw.like(User::getNickname,userDto.getNickName());//模糊查询
//        }
//
//        lqw.eq(User::getRoleId,userDto.getRoleId());
//        userService.page(page,lqw);
//        return ResultVo.ok(page);
        // 检查userDto是否为null
        if (userDto == null) {
            return ResultVo.fail("参数不能为空");
        }

        // 检查分页参数
        if (userDto.getCurrentPage() == null || userDto.getPageSize() == null) {
            return ResultVo.fail("分页参数不能为空");
        }

        Page<User> page = new Page<>(userDto.getCurrentPage(), userDto.getPageSize());
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();

        // 优化字符串判断
        if (StringUtils.hasText(userDto.getNickName())) {
            lqw.like(User::getNickname, userDto.getNickName());
        }

        // 检查roleId是否为null
        if (userDto.getRoleId() != null) {
            lqw.eq(User::getRoleId, userDto.getRoleId());
        }

        userService.page(page, lqw);
        return ResultVo.ok(page);
    }
    @Operation(summary = "查询所有用户-分页")
    @PostMapping("/findUserPageByRoleId")
    public ResultVo findAllUserPageByRoleId(@RequestBody UserDto userDto) {
        Page<User> page = new Page<>(userDto.getCurrentPage(), userDto.getPageSize());
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
       userService.page(page,lqw);
       return ResultVo.ok(page);
        }


//    @Operation(summary = "添加用户")
//    @PostMapping("/addUser")
//    public ResultVo addUser(@RequestBody User user) {
//        User user2= userService.getOne(new LambdaUpdateWrapper<User>().eq(User::getUsername, user.getPhoneNumber()));
//        if (user2!= null){
//            LambdaUpdateWrapper<User> lum=new LambdaUpdateWrapper<>();
//           lum.eq(User::getPhoneNumber,user.getPhoneNumber().set(User::getIsDeleted,0));
//          userService.update(lum);
//            return ResultVo.fail("该用户已存在");
//        }
//
//        userService.save(user);
//        return ResultVo.ok("添加成功");
//    }
@PostMapping("/addUser")
@Operation(summary = "添加用户")
public ResultVo addUser(@RequestBody User user) {
    // 查询条件：手机号等于传入的手机号（无论是否被删除）
    User user2 = userService.getOne(new LambdaQueryWrapper<User>()
            .eq(User::getPhoneNumber, user.getPhoneNumber()));

    if (user2 != null) {
        // 如果用户存在但被删除，则恢复（将isDeleted设置为0）
        if (user2.getIsDeleted() == 1) {
            LambdaUpdateWrapper<User> luw = new LambdaUpdateWrapper<>();
            luw.eq(User::getId, user2.getId())
                    .set(User::getIsDeleted, 0);
            userService.update(null, luw);
        }
        return ResultVo.ok("用户已存在，恢复成功");
    }

    // 用户不存在，插入新记录
    userService.save(user);
    return ResultVo.ok("添加成功");
}


    @Operation(summary = "修改用户")
    @PostMapping("/updateUser")
    public ResultVo updateUser(@RequestBody User user) {
      LambdaQueryWrapper<User> luw=new LambdaQueryWrapper<>();
      luw.eq(User::getId,user.getId());
      userService.update(user,luw);
      return ResultVo.ok("修改成功");
    }

    @Operation(summary = "删除用户")
    @GetMapping("/deleteUser/{id}")
    public ResultVo deleteUser(@PathVariable("id") Integer id) {
        userService.removeById(id);
        return ResultVo.ok("删除成功");
    }


    }







