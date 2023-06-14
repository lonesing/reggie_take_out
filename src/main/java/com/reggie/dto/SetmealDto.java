package com.reggie.dto;

import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @author Lyx
 * @date 2023-06-11-2:25
 * @description
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
