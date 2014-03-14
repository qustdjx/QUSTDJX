package com.qust.qustv1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.ContextMenu;
  
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;


import android.widget.AdapterView;

import android.widget.EditText;

import android.widget.ListView;
import android.widget.SimpleAdapter;


import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.qust.qustv1.MyDatabaseHelper;


public class UserAddActivity extends Activity{
	

	
	private ListView listview;
	private int count;
	private int iditem;
	private EditText txtEdit=null;
	private EditText edit=null;
	

	
	//private EditText txtEdit; //菜单中的EditText
	
	private MyDatabaseHelper dbHelper;
	  
	@SuppressLint("SdCardPath")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.useraddactivity);
		//editText=(EditText)findViewById(R.id.title);
		listview=(ListView)findViewById(R.id.listview);

	   dbHelper=new MyDatabaseHelper(this,"qust_menu_1.db3",2);
		//获取qust_menu.db3的被容并显示
	    //test();
		menuShow();
  
	}

	
	public void UserAdd(View v) {
		
		  edit=new EditText(this);
		//1.查询出最大的ID号
		// 得到一个只读的SQLiteDatabase对象  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.rawQuery("select * from menu",null);  
          count=cursor.getCount();
          Toast.makeText(this, "共有" + count + "个条目", Toast.LENGTH_LONG).show();
		//2. 写入数据库,调用编辑框
            new AlertDialog.Builder(this).setIcon(R.drawable.xiaohu).setTitle("菜单编辑")
			.setView(edit).setPositiveButton("确定",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
                //获得txt内容
				String 	txt=edit.getText().toString();
				//写入数据库
				insertData(count,txt);

					//调用menuShow 刷新显示
			         menuShow();
				}
			}).setNegativeButton("取消",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();   
            
            
            
            
            
            
            
            
            
            
            
            
		
		

	   }

	public void shake_activity_back(View v)
	{
		this.finish();
	}
	
	protected void onStop() {
		super.onStop();
		
	}
	
	public void editMenu(View v)
	{
		
	}
	


	
	
	
	/**
	 * 长按菜单响应函数[为“删除”、“编辑”菜单添加事件]
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Toast.makeText(this, "点击了长按菜单里面的第" + item.getItemId() + "个菜单", Toast.LENGTH_LONG).show();
		//定位listview哪个item出发了菜单事件
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		iditem= (int)info.id;
		 
		
		
		switch (item.getItemId()) {
		case 0:
			
			//编辑
			  txtEdit=new EditText(this);
			Toast.makeText(this, "listview里面的第" +iditem+ "个item", Toast.LENGTH_LONG).show();
			//
			new AlertDialog.Builder(this).setIcon(R.drawable.xiaohu).setTitle("菜单编辑")
			.setView(txtEdit).setPositiveButton("确定",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//修改菜单
					String txt =txtEdit.getText().toString();
					//更新数据
				
					updateData(iditem,txt);
					//menuShow重新显示
					menuShow();
					
					
				}
			}).setNegativeButton("取消",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).create().show();
			
			
			
			
			break;
		case 1:
			//删除某条记录
			deleteData(iditem);
			//重现listview
			 menuShow();
			
		default:
			System.out.println("跳出操作了");
			break;
		}
		return super.onContextItemSelected(item);
	}
	/** ===================== 为listView添加事件End ===================== */
	//显示菜单内容
	private void menuShow()
	{
		
		  //将menuname显示在listview中
	       List<Map<String, Object>> userlist = new ArrayList<Map<String, Object>>();
	       Map<String, Object> map; 
		  
		   
		  
           // 得到一个只读的SQLiteDatabase对象  
          SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  
        // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象  
       
        Cursor cursor = sqliteDatabase.query("menu", new String[] { "menu_id",  
        "menu_name" }, null, null, null, null, null);  
         // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false  
        
      while (cursor.moveToNext()) {  
       String menu_id = cursor.getString(cursor.getColumnIndex("menu_id"));  
       String  menuname = cursor.getString(cursor.getColumnIndex("menu_name"));  
       Log.d("tag","menu_id=" +menu_id+";"+ "menuname="+menuname);
          //打印调试
       
          map = new HashMap<String, Object>();
	      map.put("title",menu_id);
		  map.put("info", menuname );
		  map.put("img", R.drawable.xiaohu);
		  userlist.add(map);
		  

       
}  

        
       SimpleAdapter adapter = new SimpleAdapter(this,userlist,R.layout.vlist,
				new String[]{"title","info","img"},
				new int[]{R.id.title,R.id.info,R.id.img});
       listview.setAdapter(adapter);
       
       
       /** ===================== 为listView添加事件Start ===================== */
		// 添加点击事件[获取点击具体是哪一条数据]
         listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setTitle("点击的是第" + position + "个Item");
		Toast.makeText(UserAddActivity.this,"点击的是第" + position + "个Item", Toast.LENGTH_LONG).show();	

			}
		});
       
   	// 长按listView时添加菜单[添加“编辑”和“删除”菜单]
     
       listview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub
						menu.setHeaderTitle("长按菜单");
						menu.add(0, 0, 0, "编辑");
						menu.add(0, 1, 0, "删除");
					}
				});
       
       
     
        
	}
	
	


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//退出时关闭MyDatabaseHelper里的SQLlite database
		if(dbHelper!=null)
		{
			dbHelper.close();
		}
	}
	
	//将数据插入数据库
	private void insertData(int menuid,String menuname)
	{
		  //将菜单写入数据库
		   SQLiteDatabase sqliteDatabase_1 = dbHelper.getWritableDatabase();	
		   
		 ContentValues values = new ContentValues();  
		//将EditText的内容保存起来
			
			    values.put("menu_id", menuid);  
	            values.put("menu_name",menuname);
		sqliteDatabase_1.insert("menu", null, values);
	
	}
	
	private void deleteData(int id)
	{
		// 得到一个只读的SQLiteDatabase对象  
        SQLiteDatabase sqliteDatabase_r = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase_r.rawQuery("select * from menu",null);  
	    int count=cursor.getCount();
	    
		 //获得可写的SQLiteDatabase对象  
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        sqliteDatabase.delete("menu", "menu_id=?", new String[]{id+""}); 
          //更新id
        for(int i=id+1;i<count;i++)
        {
        	  // 创建一个ContentValues对象  
            ContentValues values = new ContentValues();  
            values.put("menu_id",(i-1)+"");  
        	sqliteDatabase.update("menu", values,  "menu_id=?", new String[] { i+"" });
        }
        
        
        
		
	}

	private void updateData(int id,String name)
	{
		// 得到一个只读的SQLiteDatabase对象  
		 SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
		 ContentValues values = new ContentValues();  
         values.put("menu_name",name);  
         sqliteDatabase.update("menu", values,  "menu_id=?", new String[] { id+"" });
		 
	}
	
	private void test()
	{
		//测试表中到底是什么
		SQLiteDatabase sqliteDatabase_r = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase_r.rawQuery("select * from menu",null);  
		 while (cursor.moveToNext()) {  
			 
			 String menu_id = cursor.getString(cursor.getColumnIndex("menu_id"));  
		     String  menuname = cursor.getString(cursor.getColumnIndex("menu_name")); 
		    // System.out.println("menu_id=" +menu_id+";"+ "menuname="+menuname);  
		 	 
			 
		 }
	}
}
