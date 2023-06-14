package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Lyx
 * @date 2023-06-12-4:26
 * @description
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
