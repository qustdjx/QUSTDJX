package com.qust.qustv1;





import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import com.qust.qustv1.ShakeListener.OnShakeListener;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	
	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private RelativeLayout mTitle;
	
	

	private SlidingDrawer mDrawer;
	private Button mDrawerBtn;
	
	private MediaPlayer mediaPlayer;
	private MyDatabaseHelper dbHelper;
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_activity);
		mVibrator = (Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
		
		//数据访问
		dbHelper=new MyDatabaseHelper(this,"qust_menu_1.db3",2);
		
		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
		mTitle = (RelativeLayout) findViewById(R.id.shake_title_bar);
		
		mDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
        mDrawerBtn = (Button) findViewById(R.id.handle);
        mDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
		{	public void onDrawerOpened()
			{	
				mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shake_report_dragger_down));
				TranslateAnimation titleup = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1.0f);
				titleup.setDuration(200);
				titleup.setFillAfter(true);
				mTitle.startAnimation(titleup);
			}
		});
		 /* 设定SlidingDrawer被关闭的事件处理 */
		
		mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
		{	public void onDrawerClosed()
			{	
				mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shake_report_dragger_up));
				TranslateAnimation titledn = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0f);
				titledn.setDuration(200);
				titledn.setFillAfter(false);
				mTitle.startAnimation(titledn);
			}
		});
		
		mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				//Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
				startAnim();  //开始 摇一摇手掌动画
				mShakeListener.stop();
				//音频播放
				palyMusic();
				startVibrato(); //开始 震动
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						//Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", 500).setGravity(Gravity.CENTER,0,0).show();
						
						
						    menuShowShake();
						      //Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
							   mVibrator.cancel();
							   mShakeListener.start();
					}
				}, 2000);
			}
		});
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startAnim () {   //定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);
		
		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);	
	}
	public void startVibrato(){		//定义震动
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}
	
	public void shake_activity_back(View v) {     //标题栏 返回按钮
      	this.finish();
      }  
	public void linshi(View v) {    
		
		//跳转至用户添加界面
		
		Intent useradd_Intent=new Intent();
		//intent 传值
		useradd_Intent.putExtra("extra", "archie2010");
		useradd_Intent.setClass(MainActivity.this,UserAddActivity.class);
		
		startActivity(useradd_Intent);
		
		
		
	
      }  
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}
    protected void onRestart()
    {  
    	super.onRestart();
    	mShakeListener.start();	
    }
	public void gone_back(View v)
	{
		//android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	
	public void palyMusic()
	{
		mediaPlayer=MediaPlayer.create(this,R.raw.thewomen);  
		mediaPlayer.start();  
	        
		 
		 //音频播放结束事件
		 mediaPlayer.setOnCompletionListener(new OnCompletionListener(){  
             @Override  
             public void onCompletion(MediaPlayer mp) {  
            	 mediaPlayer.release();//释放音频资源  
                 Toast.makeText(getApplicationContext(), "释放音频资源", Toast.LENGTH_SHORT).show();
             }  
         });  
		 //音频播放之前的准备
		
		 try{
		     mediaPlayer.prepare();
		    
		     mediaPlayer.start();
		 }catch (IllegalStateException e) {  
			
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
          
         }  
		 
	}
	
	private void menuShowShake()
	{
		//获得数据库中最大的记录数
		
		
        SQLiteDatabase sqliteDatabase_r = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase_r.rawQuery("select * from menu",null);  
	    int count=cursor.getCount();
	    //产生随机数
	     Random random = new Random(); 
		 int num=random.nextInt(count);
		 Toast.makeText(this, "随机数为："+num, Toast.LENGTH_LONG).show();
		 Log.d("tag", num+"");
		 cursor.close();
		 Cursor cursor_1=sqliteDatabase_r.query("menu", new String[] { "menu_id",  
	        "menu_name" }, "menu_id=?", new String[] {num+""}, null, null, null);
		 while (cursor_1.moveToNext()) {  
		       String menu_id = cursor_1.getString(cursor_1.getColumnIndex("menu_id"));  
		       String  menuname = cursor_1.getString(cursor_1.getColumnIndex("menu_name"));  
		       Log.d("tag","menu_id=" +menu_id+";"+ "menuname="+menuname);
		       
		      // Toast.makeText(this, "你选的菜单为："+menuname, Toast.LENGTH_LONG).show();     
		          //打印调试
		       
		       Toast mtoast;
				mtoast = Toast.makeText(getApplicationContext(),
					     "你选的菜单为："+menuname, 10);
					   mtoast.setGravity(Gravity.CENTER, 0, 0);
					   mtoast.show();
		           
		 }
		 
	    
	}
	
}
