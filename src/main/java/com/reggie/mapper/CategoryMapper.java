package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Lyx
 * @date 2023-06-09-23:47
 * @description
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
