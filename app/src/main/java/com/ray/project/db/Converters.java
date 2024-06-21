package com.ray.project.db;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * @Description: Room 底层基于 SQLite 所以只能存储基本型数据，任何对象类型必须通过 TypeConvert 转化为基本型
 * @Author: ray
 * @Date: 20/6/2024
 */
public class Converters {
    @TypeConverter
    public Date fromString(String value) {
        return new Date(Long.parseLong(value));
    }

    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
