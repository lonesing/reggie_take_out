package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表dish
        this.save(dishDto);

        Long dishid = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishid);
            return item;
        }).collect(Collectors.toList());

        //批量保存菜品口味到菜品数据表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息，用于修改时回显
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查
        Dish dish = this.getById(id);

        // 将dish数据拷贝到dishDto中
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        // 查询当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 更新菜品信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表的信息
        this.updateById(dishDto);
        // 清理当前菜品口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 添加提交过来的口味数据（给每一个DishFlavor对象绑定一个dishId）
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 批量保存口味
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品信息，同时删除对应的口味信息
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {

        // 先判断销售状态
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ids!=null,Dish::getId,ids);// 添加条件：在传入的菜品id中进行查询
        queryWrapper.eq(Dish::getStatus,1);// 添加条件：查询正在售卖的
        int count = (int) this.count(queryWrapper);// 查询传入的id中正在售卖的有多少个
        if(count>0){
            //如果数个大于0，表示不能删除，抛出一个业务异常
            throw new CustomException("选中的菜品有正在售卖中，不能删除");
        } else{
            // 删除菜品信息，removeByIds是根据id集合批量删除
            this.removeByIds(ids);
            // 删除口味信息
            LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
            flavorQueryWrapper.in(DishFlavor::getDishId,ids);
            // delete from dish_flavor where id in (ids)
            dishFlavorService.remove(flavorQueryWrapper);
        }
    }

    /**
     * 修改售卖状态
     * @param status
     * @param ids
     */
    @Override
    public void updateStatus(Integer status,List<Long> ids) {
        // 这里需要修改状态，所以使用LambdaUpdateWrapper，
        // 该对象既有查询功能也有修改功能，该对象中有set方法可以修改字段值
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.in(ids!=null,Dish::getId,ids); // 添加更新条件：在ids集合中查询
        updateWrapper.set(Dish::getStatus,status);// 修改菜品的售卖状态
        this.update(updateWrapper);// 执行更新操作，将更新条件传进去
    }
}
