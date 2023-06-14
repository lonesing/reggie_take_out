package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Lyx
 * @date 2023-06-12-3:44
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
