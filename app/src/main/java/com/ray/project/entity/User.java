package com.ray.project.entity;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @Description: room数据库User表
 * 默认情况下，Room 将类名称用作数据库表名称
 * 如果希望表具有不同的名称，请设置 @Entity 注释的 tableName 属性【个人建议】
 * @Author: ray
 * @Date: 20/6/2024
 */
@Entity(tableName = "users")
public class User extends BaseBean {
    // 主键，自动增长
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public int uid;

    // 与 tableName 属性类似，Room 将字段名称用作数据库中的列名称
    // 如果希望列具有不同的名称，请将 @ColumnInfo 注释添加到字段【个人建议】
    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    // 默认情况下，Room 会为实体中定义的每个字段创建一个列
    // 如果某个实体中有不想保留的字段，则可以使用 @Ignore 为这些字段添加注释
    @Ignore
    public Bitmap picture;

    // 嵌套对象
    @Embedded
    public Address address;
}
