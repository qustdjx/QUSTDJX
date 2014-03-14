package com.qust.qustv1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	private final String CREATE_TABLE_SQL="Create table menu(menu_id int primary key,menu_name varchar(20))";
    private static final int VERSION = 1; 
    
	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
		
		
	}
	public MyDatabaseHelper(Context context, String name, int version){  
        this(context,name,null,version);  
    }  
  
	public MyDatabaseHelper(Context context, String name){  
        this(context,name,VERSION);  
    }  

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
	

}
