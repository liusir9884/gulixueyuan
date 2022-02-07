package com.liu.educenter.mapper;

import com.liu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-01-09
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {


    int countRegisterDay(String day);

}
