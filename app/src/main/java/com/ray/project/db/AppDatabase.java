package com.ray.project.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ray.project.commons.Logger;
import com.ray.project.db.dao.UserDao;
import com.ray.project.entity.User;

/**
 * @Description: room数据库
 * 多张表@Database(entities = {Department.class, Company.class}, version = 1, exportSchema = false)
 * @Author: ray
 * @Date: 20/6/2024
 */

@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters(value = {Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    public static final String DB_NAME = "RayDatabase.db";
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();

    // 在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，几乎不需要在单个进程中访问多个实例。
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME
                            ).addMigrations()
                            .addCallback(roomCallback)
                            // 默认不允许在主线程中连接数据库
//                            .allowMainThreadQueries()
                            .build();

                    /*
                     * 1. 假如要在User中添加phoneNum
                        @ColumnInfo(name = "phone_num")
                        private String phoneNum;
                     * 2. 把上面的版本号自增version = 2
                     * 3. 添加一个
                        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
                            @Override
                            public void migrate(SupportSQLiteDatabase database) {
                                database.execSQL("ALTER TABLE department "
                                    + " ADD COLUMN phone_num TEXT");
                            }
                        };
                     * 4. 把migration添加到databaseBuilder中
                        Room.databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                DB_NAME
                            ).addMigrations(MIGRATION_1_2)
                            .addCallback(roomCallback)
                            .allowMainThreadQueries()
                            // 默认不允许在主线程中连接数据库
//                            .allowMainThreadQueries()
                            .build();
                     */
                }
            }
        }
        return instance;
    }

    // 回调函数，可在数据库创建、打开和关闭时执行操作
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Logger.d("AppDatabase", "Database created successfully");
        }

    };
}
