package com.liu.educenter.service;

import com.liu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.educenter.entity.vo.UcenterQuery;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-01-09
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);


    void register(UcenterQuery ucenterQuery);

    UcenterMember getOpenIdMember(String openid);

    int countRegisterDay(String day);
}
