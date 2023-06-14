package com.reggie.dto;

import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lyx
 * @date 2023-06-10-17:37
 * @description 数据传输对象
 */
@Data
public class DishDto extends Dish {
    // 因为发送的数据是一个DishFlavor类型的数据，所以用List<DishFlavor>接收。
    private List<DishFlavor> flavors = new ArrayList<>();
    // 下面两个属性在后续中会使用
    private String categoryName;
    private Integer copies;
}
