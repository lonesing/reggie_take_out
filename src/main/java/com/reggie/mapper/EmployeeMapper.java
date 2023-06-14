package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Lyx
 * @date 2023-06-08-18:02
 * @description
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
