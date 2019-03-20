package com.guli.ucenter.service;

import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Wanba
 * @since 2019-03-12
 */
public interface MemberService extends IService<Member> {

    Integer countRegisterByDay(String day);

    Member getByOpenid(String openid);
}
