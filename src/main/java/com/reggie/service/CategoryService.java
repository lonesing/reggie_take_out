package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.Category;

/**
 * @author Lyx
 * @date 2023-06-09-23:47
 * @description
 */
public interface CategoryService extends IService<Category> {
    // 自定义删除方法
    public void remove(Long id);
}
