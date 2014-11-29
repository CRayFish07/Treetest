package com.gao.tree;   
  

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;  
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.geniuseoe.spiner.demo.widget.CustemObject;

import android.app.Activity;   
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;   
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;   
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;   
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;   
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;   
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;   
import android.widget.TextView;
import android.widget.Toast;
 
public class UserListView extends Activity {   
   
    private ListView listView;
    private HorizontalScrollView layout;
    private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
    private String strCurrUser,strCurRecvUnit;  
    public  int iUpdateRowNum=0;
    public  String strUpdateState;
    private DBHelper dbHelper;    
    UserAdapter adapter;
    private Intent newintent;
	private int iWidth,iHeight;
	private static final int ITEM = Menu.FIRST;
	private static final int ITEM1 = Menu.FIRST+ 3;
	private static final int ITME2 = Menu.FIRST + 1;
	private static final int ITME3 = Menu.FIRST + 2;
    public void onCreate(Bundle savedInstanceState) {  
    	//requestWindowFeature( Window.FEATURE_NO_TITLE ); //�ޱ���
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ��Ϊ����
        super.onCreate(savedInstanceState); 
        View bv = this.findViewById(android.R.id.title);
        ((TextView) bv).setTextColor(Color.BLACK);
        ((TextView) bv).setTextSize(15);
        ((View) bv.getParent()).setBackgroundResource(R.drawable.background_login);
       setContentView(R.layout.usertable);    
       listView = (ListView) findViewById(R.id.listview);
       layout = (HorizontalScrollView) findViewById(R.id.layout);
       dbHelper=new DBHelper(this);	   
       Display display = getWindowManager().getDefaultDisplay();
       iWidth=display.getWidth();
       iHeight=display.getHeight();
       listView.setOnItemClickListener(new ListView.OnItemClickListener() {  
        	  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                // TODO Auto-generated method stub  
            	adapter.setSelectedPosition(arg2); 
            	adapter.notifyDataSetInvalidated();     
  
            }  
        });  
      //���ó����˵���
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener()
        {       
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {	
				AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) menuInfo;
				adapter  = new UserAdapter(UserListView.this, lists,iWidth,iHeight);
				ArrayList<String> list =  (ArrayList<String>) adapter.getItem(contextMenuInfo.position);		  		  
			    menu.setHeaderTitle("��ѡ����Ҫ���еĲ���");
			    if(!list.get(1).equals("administrator"));
                    menu.add(0, 0, 0, "ɾ���û�");
                menu.add(0, 1, 0, "����û�");
                menu.add(0, 2, 0, "�޸�����");    			    
			}
        }); 
        InitHeaderData();	       
	    LoadUserData();
	   	   
    }    
	public  void   InitHeaderData()
    {
    	 ArrayList<String> list = new ArrayList<String>();
         list.add("���");
         list.add("�û���");
         list.add("��������");
         list.add("����Ȩ��");
         lists.add(list);
         adapter  = new UserAdapter(UserListView.this, lists,iWidth,iHeight);       
	     listView.setAdapter(adapter);
	     layout.setVisibility(View.VISIBLE);         
    }  
    @Override
    public void onConfigurationChanged(Configuration newConfig) {    
        super.onConfigurationChanged(newConfig);
        // �����Ļ�ķ�����������
        if (this.getResources().getConfiguration().orientation 
                == Configuration.ORIENTATION_LANDSCAPE) {
            //��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
        }
        else if (this.getResources().getConfiguration().orientation 
                == Configuration.ORIENTATION_PORTRAIT) {
            //��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
        }
        //���ʵ����̵�״̬���Ƴ����ߺ���    
        if (newConfig.hardKeyboardHidden 
                == Configuration.HARDKEYBOARDHIDDEN_NO){ 
            //ʵ����̴����Ƴ�״̬���ڴ˴���Ӷ���Ĵ������
        } 
        else if (newConfig.hardKeyboardHidden
                == Configuration.HARDKEYBOARDHIDDEN_YES){ 
            //ʵ����̴��ں���״̬���ڴ˴���Ӷ���Ĵ������
        }
    }
 
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int iPos= (int) info.position;// �����info.id��Ӧ�ľ������ݿ���_id��ֵ
        adapter  = new UserAdapter(UserListView.this, lists,iWidth,iHeight);
		ArrayList<String> list =  (ArrayList<String>) adapter.getItem(iPos);
        switch (item.getItemId()) 
        {
            case 0:    	            		
            	String strSql=String.format("delete from userinfo where name='%s' and pwd='%s' and department='%s'", list.get(1), list.get(2), list.get(3));
            	ExecSQL(strSql);
            	dbHelper.DeleteUser(list.get(1), list.get(2), list.get(3));      
            	adapter.remove(iPos);            	
            	listView.setAdapter(adapter);
                layout.setVisibility(View.VISIBLE);
                Intent broad = new Intent();
                broad.setAction("action.refreshFriend");
		        sendBroadcast(broad);
                break;
            case 1:               		
            	Intent intent=new Intent(UserListView.this,RegisterActivity.class);
        		Bundle bundle=new Bundle();          	
        		bundle.putInt("DepartCount", LoginActivity.iDepartCount);
                bundle.putInt("UserCount", LoginActivity.iUserCount);
                bundle.putStringArray("Department", LoginActivity.strDepartment);
                intent.putExtras(bundle);
    			startActivity(intent);		
    			break;               
            case 2:               	
            	Intent intent1=new Intent(UserListView.this,ModifyActivity.class);
        		Bundle bundle1=new Bundle();          	
        		bundle1.putBoolean("AdminUser", true);
        		bundle1.putString("CurUser", list.get(1));          
                intent1.putExtras(bundle1);
    			startActivity(intent1);		
                break;
            default:            	
            	break;
        }
        return super.onContextItemSelected(item);
    }
   
	public void LoadUserData()
    {   
		
		for(int i = 0; i < LoginActivity.iUserCount; i++)
		{
			ArrayList<String> list = new ArrayList<String>();		
			list.add(""+adapter.getCount());
	    	list.add(LoginActivity.strUName[i]);
	    	list.add(LoginActivity.strUDepart[i]); 
	    	list.add(LoginActivity.strUType[i]);
	    	lists.add(list);
			
		} 	
		
		listView.setAdapter(adapter);
		layout.setVisibility(View.VISIBLE);  
    }
    private void ExecSQL(final String strSql)	
	{
		Runnable run = new Runnable()
		{			
			@Override
			public void run()
			{
				if(LoginActivity.TestConnect())
				    DataBaseUtil.ExecSQL(strSql);
				
			}
		};
		new Thread(run).start();
		 
	}     
 
    public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add("һ��������add�����Ĳ˵�");
		// menu.add("һ��������add�����Ĳ˵�2");
		 //menu.add(R.string.titileRes);
    	menu.add(0, ITEM, 0, "ע���û�").setIcon(R.drawable.user);
    	menu.add(0, ITEM1, 0, "�޸�����").setIcon(R.drawable.changepwd);
    	menu.add(0, ITME2, 0, "�����ɸ�").setIcon(R.drawable.feige);
		menu.add(0, ITME3, 0, "�˳�ϵͳ").setIcon(R.drawable.exitsys);
		

		//menu.add(0, ITEM, 0, "����").setIcon(R.drawable.download);//����ͼ��
		//menu.add(0, ITME2, 0, "�ϴ�").setIcon(R.drawable.upload);

		return true;
	}

	// ͨ��������ĸ��˵��������ı�Activity�ı���
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case ITEM:
			Intent intent=new Intent(UserListView.this,RegisterActivity.class);
    		Bundle bundle=new Bundle();          	
    		bundle.putInt("DepartCount", LoginActivity.iDepartCount);
            bundle.putInt("UserCount", LoginActivity.iUserCount);
            bundle.putStringArray("Department", LoginActivity.strDepartment);
            intent.putExtras(bundle);
			startActivity(intent);		
			break;
		case ITEM1:
			Intent intent1=new Intent(UserListView.this,ModifyActivity.class);
    		Bundle bundle1=new Bundle();          	
    		bundle1.putBoolean("AdminUser", true);
    		bundle1.putString("CurUser", "Administrator");          
            intent1.putExtras(bundle1);
			startActivity(intent1);		
			break;	
		case ITME2:
			launchApp("com.netfeige");
			break;
		case ITME3:
			AlertDialog.Builder builder = new Builder(UserListView.this); 
     		builder.setIcon(android.R.drawable.ic_dialog_info);
 	        builder.setMessage("ȷ��Ҫ�˳�?"); 
 	        builder.setTitle("��ʾ"); 
 	        builder.setPositiveButton("ȷ��", 
 	                new android.content.DialogInterface.OnClickListener() { 
 	                    public void onClick(DialogInterface dialog, int which) { 
 	                        dialog.dismiss(); 
 	                      int currentVersion = android.os.Build.VERSION.SDK_INT;  
 	                    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {  
 	                        Intent startMain = new Intent(Intent.ACTION_MAIN);  
 	                        startMain.addCategory(Intent.CATEGORY_HOME);  
 	                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
 	                        startActivity(startMain);  
 	                        System.exit(0);  
 	                    } else {// android2.1  
 	                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);  
 	                        am.restartPackage(getPackageName());  
 	                    }   
 	                    } 
 	                }); 
 	        builder.setNegativeButton("ȡ��", 
 	                new android.content.DialogInterface.OnClickListener() { 
 	                    public void onClick(DialogInterface dialog, int which) { 
 	                        dialog.dismiss(); 
 	                    } 
 	                }); 
 	        		builder.create().show(); 
 	        		break;
			
			

		}
		return true;
	}
	public void launchApp(String pocketName) {
		PackageManager packageManager = this.getPackageManager();
		List<PackageInfo> packages = getAllApps();
		PackageInfo pa = null;
		for(int i=0;i<packages.size();i++){
			pa = packages.get(i);
			//���Ӧ����
			String appLabel = packageManager.getApplicationLabel(pa.applicationInfo).toString();
			//��ð���
			String appPackage = pa.packageName;
			Log.d(""+i, appLabel+"  "+appPackage);
		}
		newintent = packageManager.getLaunchIntentForPackage(pocketName);
		startActivity(newintent);
	}
	
	public List<PackageInfo> getAllApps() {  
	    List<PackageInfo> apps = new ArrayList<PackageInfo>();  
	    PackageManager pManager = this.getPackageManager();  
	    //��ȡ�ֻ�������Ӧ��  
	    List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
	    for (int i = 0; i < paklist.size(); i++) {  
	        PackageInfo pak = (PackageInfo) paklist.get(i);  
	        //�ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ��  (����0ΪϵͳԤװӦ��)
	        if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {  
	            apps.add(pak);  
	        }  
	    }  
	    return apps;  
	}  
}  