package com.cy.service;

import com.cy.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.utils.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author div F
 * @since 2025-06-23
 */
public interface IUserService extends IService<User> {
    //登录接口
    ResultVo login(User user);

}
