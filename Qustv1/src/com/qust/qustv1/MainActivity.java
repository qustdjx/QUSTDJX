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
		
		//���ݷ���
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
		 /* �趨SlidingDrawer���رյ��¼����� */
		
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
				//Toast.makeText(getApplicationContext(), "��Ǹ����ʱû���ҵ���ͬһʱ��ҡһҡ���ˡ�\n����һ�ΰɣ�", Toast.LENGTH_SHORT).show();
				startAnim();  //��ʼ ҡһҡ���ƶ���
				mShakeListener.stop();
				//��Ƶ����
				palyMusic();
				startVibrato(); //��ʼ ��
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						//Toast.makeText(getApplicationContext(), "��Ǹ����ʱû���ҵ�\n��ͬһʱ��ҡһҡ���ˡ�\n����һ�ΰɣ�", 500).setGravity(Gravity.CENTER,0,0).show();
						
						
						    menuShowShake();
						      //Toast.makeText(getApplicationContext(), "��Ǹ����ʱû���ҵ���ͬһʱ��ҡһҡ���ˡ�\n����һ�ΰɣ�", Toast.LENGTH_SHORT).show();
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
	
	public void startAnim () {   //����ҡһҡ��������
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
	public void startVibrato(){		//������
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //��һ�����������ǽ������飬 �ڶ����������ظ�������-1Ϊ���ظ�����-1���մ�pattern��ָ���±꿪ʼ�ظ�
	}
	
	public void shake_activity_back(View v) {     //������ ���ذ�ť
      	this.finish();
      }  
	public void linshi(View v) {    
		
		//��ת���û���ӽ���
		
		Intent useradd_Intent=new Intent();
		//intent ��ֵ
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
	        
		 
		 //��Ƶ���Ž����¼�
		 mediaPlayer.setOnCompletionListener(new OnCompletionListener(){  
             @Override  
             public void onCompletion(MediaPlayer mp) {  
            	 mediaPlayer.release();//�ͷ���Ƶ��Դ  
                 Toast.makeText(getApplicationContext(), "�ͷ���Ƶ��Դ", Toast.LENGTH_SHORT).show();
             }  
         });  
		 //��Ƶ����֮ǰ��׼��
		
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
		//������ݿ������ļ�¼��
		
		
        SQLiteDatabase sqliteDatabase_r = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase_r.rawQuery("select * from menu",null);  
	    int count=cursor.getCount();
	    //���������
	     Random random = new Random(); 
		 int num=random.nextInt(count);
		 Toast.makeText(this, "�����Ϊ��"+num, Toast.LENGTH_LONG).show();
		 Log.d("tag", num+"");
		 cursor.close();
		 Cursor cursor_1=sqliteDatabase_r.query("menu", new String[] { "menu_id",  
	        "menu_name" }, "menu_id=?", new String[] {num+""}, null, null, null);
		 while (cursor_1.moveToNext()) {  
		       String menu_id = cursor_1.getString(cursor_1.getColumnIndex("menu_id"));  
		       String  menuname = cursor_1.getString(cursor_1.getColumnIndex("menu_name"));  
		       Log.d("tag","menu_id=" +menu_id+";"+ "menuname="+menuname);
		       
		      // Toast.makeText(this, "��ѡ�Ĳ˵�Ϊ��"+menuname, Toast.LENGTH_LONG).show();     
		          //��ӡ����
		       
		       Toast mtoast;
				mtoast = Toast.makeText(getApplicationContext(),
					     "��ѡ�Ĳ˵�Ϊ��"+menuname, 10);
					   mtoast.setGravity(Gravity.CENTER, 0, 0);
					   mtoast.show();
		           
		 }
		 
	    
	}
	
}
