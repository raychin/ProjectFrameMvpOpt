package com.ray.project.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ray.project.entity.User;

import java.util.List;

/**
 * @Description: db user dao
 * @Query注解，用于查询操作，可以返回单个对象或者对象列表，数组应该也支持
 * @Insert注解，用于插入操作，参数为列表，可以支持批量插入。插入方法是否支持返回值，待验证
 * @Update注解，用于更新操作，可以通过WHERE条件，更新单条或者多条数据
 * @Delete注解，用于删除操作，可以通过WHERE条件，删除单条或者多条数据
 * @Query注解除了查询功能外，还支持增删改操作，用法基本一样，只是它的返回值支持void和int类型
 * @Author: ray
 * @Date: 21/6/2024
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Query("SELECT * FROM users WHERE uid = :uId")
    User findById(long uId);

    @Insert
    long insertUser(User user);

    @Delete
    int delete(User user);
}
