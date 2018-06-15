package com.zqy.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zqy.sqlitedemo.db.DBManager;
import com.zqy.sqlitedemo.db.SqliteHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_des)
    TextView des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.create, R.id.upgrade, R.id.insert, R.id.modify, R.id.query, R.id.delete, R.id.delete_db,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create: {
                // 创建数据库

                // 创建SQLiteOpenHelper子类对象
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this);
                /**
                 * 数据库实际上是没有被创建或者打开的，直到getWritableDatabase()
                 * 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
                 * SQLiteDatabase database = dbHelper.getWritableDatabase();
                 * 或 SQLiteDatabase database = dbHelper.getReadbleDatabase();
                 */
                Toast.makeText(this, "创建数据库成功", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.upgrade: {
                // 更新数据库

                // 创建SQLiteOpenHelper子类对象
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this, 2);
                /**
                 * 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                 * SQLiteDatabase database = dbHelper.getWritableDatabase();
                 * SQLiteDatabase database = dbHelper.getReadbleDatabase();
                 */
                Toast.makeText(this, "更新数据库成功", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.insert: {
                // 插入数据
                System.out.println("插入数据");

                // 1. 创建SQLiteOpenHelper子类对象
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this, 2);
                // 2. 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase  database = dbHelper.getWritableDatabase();

                // 3. 创建ContentValues对象
                ContentValues values = new ContentValues();

                // 4. 向该对象中插入键值对
                int id = 1;
                String name = "zhaoqy";
                values.put("id", id);
                values.put("name", name);

                // 5. 调用insert()方法将数据插入到数据库当中
                database.insert(DBManager.TABLE_NAME, null, values);
                // sqliteDatabase.execSQL("insert into user (id,name) values (1,'carson')");

                // 6. 关闭数据库
                database.close();

                des.setText("插入数据为：id=" + id + ", name=" + name);
                Toast.makeText(this, "插入数据成功", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.modify: {
                // 修改数据
                System.out.println("修改数据");

                // 创建一个DatabaseHelper对象
                // 将数据库的版本升级为2
                // 传入版本号为2，大于旧版本（1），所以会调用onUpgrade()升级数据库
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this, 2);

                // 调用getWritableDatabase()得到一个可写的SQLiteDatabase对象
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                // 创建一个ContentValues对象
                ContentValues values = new ContentValues();
                String name = "zhangsan";
                values.put("name", name);

                // 调用update方法修改数据库
                database.update(DBManager.TABLE_NAME, values, "id=?", new String[]{"1"});

                //关闭数据库
                database.close();

                des.setText("修改数据：name=" + name);
                Toast.makeText(this, "修改数据成功", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.query: {
                // 查询数据
                System.out.println("查询数据");

                // 创建DatabaseHelper对象
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this, 2);

                // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase database = dbHelper.getReadableDatabase();

                // 调用SQLiteDatabase对象的query方法进行查询
                // 返回一个Cursor对象：由数据库查询返回的结果集对象
                Cursor cursor = database.query("user", new String[] { "id",
                        "name" }, "id=?", new String[] { "1" }, null, null, null);

                String id = null;
                String name = null;

                // 将光标移动到下一行，从而判断该结果集是否还有下一条数据
                // 如果有则返回true，没有则返回false
                while (cursor.moveToNext()) {
                    id = cursor.getString(cursor.getColumnIndex("id"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    //输出查询结果
                    System.out.println("查询到的数据是:"+"id: "+id+"  "+"name: "+name);
                }
                //关闭数据库
                database.close();

                des.setText("查询数据：id=" + id + ", name=" + name);
                Toast.makeText(this, "查询数据成功", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.delete: {
                // 删除数据
                System.out.println("删除数据");

                // 创建DatabaseHelper对象
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this, 2);

                // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                //删除数据
                database.delete(DBManager.TABLE_NAME, "id=?", new String[]{"1"});

                //关闭数据库
                database.close();
                Toast.makeText(this, "删除数据成功", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.delete_db: {
                // 删除数据库
                System.out.println("删除数据库");

                // 创建DatabaseHelper对象
                SqliteHelper dbHelper = DBManager.getSqliteHelper(this, 2);

                // 调用getReadableDatabase()方法创建或打开一个可以读的数据库
                SQLiteDatabase database = dbHelper.getReadableDatabase();

                // 删除名为DB_NAME数据库
                deleteDatabase(DBManager.DB_NAME);
                break;
            }
        }
    }
}
