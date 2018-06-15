### Android SQLlite数据库

----在Android开发中，主要的数据存储有5种，具体如下：

![image](https://github.com/zhaoqingyue/ZQYAndroidNotes/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8/storage_method.png)

**1. SQLlite数据库简介**

（1）定义：一种嵌入式数据库。

（2）特点：

- 存储结构型、关系型数据
- 可使用SQL语言
- 支持事务处理
- 独立、无需服务进程

（3）Android中的具体实现：
- SQLiteOpenHelper类
- Android1.5后引入

**2. SQLiteOpenHelper类**

（1）简介

A. 定义：一个SQLlite数据库辅助操作类；

B. 作用：在Android中实现SQLlite数据库操作，即管理数据库（创建、新增、修改、删除）& 版本控制；

C. 使用流程：在实际开发中，为了能够更好的管理和维护数据库，会自定义1个继承自SQLliteOpenHelper类的数据库操作类，然后以该类为基础，根据业务需求，实现数据库的操作方法。

- 创建SQLlite数据库操作子类（继承SQLliteOpenHelper类）；
- 根据自身需求，实现SQLliteOpenHelper类的方法，从而实现对数据库的操作。

（2）SQLiteOpenHelper类 常用方法

```
 /** 
  *  创建数据库
  */ 
 // 1. 创建 or 打开 可读/写的数据库（通过 返回的SQLiteDatabase对象 进行操作）
 getWritableDatabase（）

 // 2. 创建 or 打开 可读的数据库（通过 返回的SQLiteDatabase对象 进行操作）
 getReadableDatabase（）

 // 3. 数据库第1次创建时 则会调用，即 第1次调用 getWritableDatabase（） / getReadableDatabase（）时调用
 // 在继承SQLiteOpenHelper类的子类中复写
 onCreate(SQLiteDatabase db) 

 // 4. 数据库升级时自动调用
 // 在继承SQLiteOpenHelper类的子类中复写
 onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

 // 5. 关闭数据库
 close（）

 /** 
  *  数据库操作（增、删、减、查）
  */ 
 // 1. 查询数据
 (Cursor) query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)  
 // 查询指定的数据表返回一个带游标的数据集。
 // 各参数说明： 
 // table：表名称 
 // colums：列名称数组 
 // selection：条件子句，相当于where 
 // selectionArgs：条件语句的参数数组 
 // groupBy：分组 
 // having：分组条件 
 // orderBy：排序类 
 // limit：分页查询的限制 
 // Cursor：返回值，相当于结果集ResultSet 

 (Cursor) rawQuery(String sql, String[] selectionArgs) 
 //运行一个预置的SQL语句，返回带游标的数据集（与上面的语句最大的区别 = 防止SQL注入）

 // 2. 删除数据行  
 (int) delete(String table,String whereClause,String[] whereArgs) 
 
 // 3. 添加数据行 
 (long) insert(String table,String nullColumnHack,ContentValues values) 
 
 // 4. 更新数据行 
 (int) update(String table, ContentValues values, String whereClause, String[] whereArgs) 
 
 // 5. 执行一个SQL语句，可以是一个select or 其他sql语句 
 // 即 直接使用String类型传入sql语句 & 执行
 (void) execSQL(String sql)

```

**3. 具体使用**

----使用步骤 = 自定义数据库子类（继承SQLiteOpenHelper类）、创建数据库 & 操作数据库（增、删、查、改）

（1）自定义数据库子类（继承 SQLiteOpenHelper 类）

```
 /** 
  * 创建数据库子类，继承自SQLiteOpenHelper类
  * 需 复写 onCreat（）、onUpgrade（）
  */ 
 public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库版本号
    private static Integer Version = 1;

    /** 
     * 构造函数
     * 在SQLiteOpenHelper的子类中，必须有该构造函数
     */ 
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        // 参数说明
        // context：上下文对象
        // name：数据库名称
        // param：一个可选的游标工厂（通常是 Null） 
        // version：当前数据库的版本，值必须是整数并且是递增的状态

        // 必须通过super调用父类的构造函数
        super(context, name, factory, version);
    }
    
    /** 
     * 复写onCreate（）
     * 调用时刻：当数据库第1次创建时调用
     * 作用：创建数据库 表 & 初始化数据
     * SQLite数据库创建支持的数据类型： 整型数据、字符串类型、日期类型、二进制
     */ 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库1张表
        // 通过execSQL（）执行SQL语句（此处创建了1个名为person的表）
        String sql = "create table person(id integer primary key autoincrement,name varchar(64),address varchar(64))"; 
        db.execSQL(sql); 

        // 注：数据库实际上是没被创建 / 打开的（因该方法还没调用）
        // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用时才会进行创建 / 打开 
    }

    /** 
     * 复写onUpgrade（）
     * 调用时刻：当数据库升级时则自动调用（即 数据库版本 发生变化时）
     * 作用：更新数据库表结构
     * 注：创建SQLiteOpenHelper子类对象时,必须传入一个version参数，该参数 = 当前数据库版本, 若该版本高于之前版本, 就调用onUpgrade()
     */ 

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 参数说明： 
        // db ： 数据库 
        // oldVersion ： 旧版本数据库 
        // newVersion ： 新版本数据库 

        // 使用 SQL的ALTER语句
        String sql = "alter table person add sex varchar(8)";  
        db.execSQL(sql);  
    }
}

```

（2）创建数据库：getWritableDatabase（）、getReadableDatabase（）

```
// 步骤1：创建DatabaseHelper对象
// 注：此时还未创建数据库
SQLiteOpenHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"test_carson");

// 步骤2：真正创建 / 打开数据库
SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase(); // 创建 or 打开 可读/写的数据库
SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase(); // 创建 or 打开 可读的数据库
```

注：当需操作数据库时，都必须先创建数据库对象 & 创建 / 打开数据库。

- 对于操作 = “增、删、改（更新）”，需获得 可"读 / 写"的权限：getWritableDatabase()
- 对于操作 = “查询”，需获得 可"读 "的权限getReadableDatabase()

（3）操作数据库（增、删、查、改）

```
/** 
 * 1. 创建 & 打开数据库
 */ 

// a. 创建DatabaseHelper对象
// 注：一定要传入最新的数据库版本号
SQLiteOpenHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"test_carson"，2);
// b.创建 or 打开 可读/写的数据库
SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

/** 
 *  操作1：插入数据 = insert()
 */ 
// a. 创建ContentValues对象
ContentValues values = new ContentValues();

// b. 向该对象中插入键值对
values.put("id", 1);
values.put("name", "carson");
//其中，key = 列名，value = 插入的值
//注：ContentValues内部实现 = HashMap，区别在于：ContenValues Key只能是String类型，Value可存储基本类型数据 & String类型

// c. 插入数据到数据库当中：insert()
sqliteDatabase.insert("user", null, values);
// 参数1：要操作的表名称
// 参数2：SQl不允许一个空列，若ContentValues是空，那么这一列被明确的指明为NULL值
// 参数3：ContentValues对象
// 注：也可采用SQL语句插入
String sql = "insert into user (id,name) values (1,'carson')";
db.execSQL(sql) ；       
            
/** 
 *  操作2：修改数据 = update（）
 */ 
// a. 创建一个ContentValues对象
ContentValues values = new ContentValues();
values.put("name", "zhangsan");

// b. 调用update方法修改数据库：将id=1 修改成 name = zhangsan
sqliteDatabase.update("user", values, "id=?", new String[] { "1" });
// 参数1：表名(String)
// 参数2：需修改的ContentValues对象
// 参数3：WHERE表达式（String），需数据更新的行； 若该参数为 null, 就会修改所有行；？号是占位符
// 参数4：WHERE选择语句的参数(String[]), 逐个替换 WHERE表达式中 的“？”占位符;

// 注：调用完upgrate（）后，则会回调 数据库子类的onUpgrade()
// 注：也可采用SQL语句修改
String sql = "update [user] set name = 'zhangsan' where id="1";
db.execSQL(sql);

/** 
 *  操作3：删除数据 = delete()
 */
// 删除 id = 1的数据
sqliteDatabase.delete("user", "id=?", new String[]{"1"});
// 参数1：表名(String)
// 参数2：WHERE表达式（String），需删除数据的行； 若该参数为 null, 就会删除所有行；？号是占位符
// 参数3：WHERE选择语句的参数(String[]), 逐个替换 WHERE表达式中 的“？”占位符;

// 注：也可采用SQL语句修改
String sql = "delete from user where id="1"；
db.execSQL(sql);

/** 
 *  操作4：查询数据1 = rawQuery() 
 *  直接调用 SELECT 语句
 */
Cursor c = db.rawQuery("select * from user where id=?",new Stirng[]{"1"}); 
// 返回值一个 cursor 对象

// 通过游标的方法可迭代查询结果
if(cursor.moveToFirst()) { 
    String password = c.getString(c.getColumnIndex("password")); 
}
        
//Cursor对象常用方法如下：
c.move(int offset); //以当前位置为参考,移动到指定行  
c.moveToFirst();    //移动到第一行  
c.moveToLast();     //移动到最后一行  
c.moveToPosition(int position); //移动到指定行  
c.moveToPrevious(); //移动到前一行  
c.moveToNext();     //移动到下一行  
c.isFirst();        //是否指向第一条  
c.isLast();     //是否指向最后一条  
c.isBeforeFirst();  //是否指向第一条之前  
c.isAfterLast();    //是否指向最后一条之后  
c.isNull(int columnIndex);  //指定列是否为空(列基数为0)  
c.isClosed();       //游标是否已关闭  
c.getCount();       //总数据项数  
c.getPosition();    //返回当前游标所指向的行数  
c.getColumnIndex(String columnName);//返回某列名对应的列索引值  
c.getString(int columnIndex);   //返回当前行指定列的值 
        
// 通过游标遍历1个名为user的表
Cursor result=db.rawQuery("SELECT _id, username, password FROM user");  
result.moveToFirst();  
while (!result.isAfterLast()) {  
    int id=result.getInt(0);  
    String name=result.getString(1);  
    String password =result.getString(2);  
    // do something useful with these  
    result.moveToNext();  
}  
result.close();


// 若查询是动态的，使用该方法会复杂。此时使用 query() 会方便很多
// 注：无法使用SQL语句，即db.execSQL(sql);

/** 
 *  操作4：查询数据2 = query() 
 *  直接调用 SELECT 语句
 */
// 方法说明
db.query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);  
db.query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);  
db.query(String distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit); 

// 参数说明
// table：要操作的表
// columns：查询的列所有名称集
// selection：WHERE之后的条件语句，可以使用占位符
// groupBy：指定分组的列名
// having指定分组条件，配合groupBy使用
// orderBy指定排序的列名
// limit指定分页参数
// distinct可以指定“true”或“false”表示要不要过滤重复值

// 所有方法将返回一个Cursor对象，代表数据集的游标 
// 具体使用
Cursor cursor = sqliteDatabase.query("user", new String[] { "id","name" }, "id=?", new String[] { "1" }, null, null, null);
// 参数1：（String）表名
// 参数2：（String[]）要查询的列名
// 参数3：（String）查询条件
// 参数4：（String[]）查询条件的参数
// 参数5：（String）对查询的结果进行分组
// 参数6：（String）对分组的结果进行限制
// 参数7：（String）对查询的结果进行排序
            
// 注：无法使用SQL语句，即db.execSQL(sql);
/** 
 *  操作5：关闭数据库 = close()
 *  注：完成数据库操作后，记得调用close（）关闭数据库，从而释放数据库的连接
 */
sqliteDatabase.close();  

/** 
 *  操作6：删除数据库 = deleteDatabase（）
 */
// 删除 名为person的数据库  
deleteDatabase("test.db");

```
