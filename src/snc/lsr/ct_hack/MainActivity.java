package snc.lsr.ct_hack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URLConnection;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import lsr.HttpRequester;
import lsr.HttpRespons;

public class MainActivity extends Activity {
	
    public MToast t = new MToast(this);
    public static String acc,pss,loginMsg;
	TextView console;
	
	private void setNotification() {
	    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	    PendingIntent contextIntent = PendingIntent.getActivity(this, 0, intent, 0);
	    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//	    Notification notification = new Notification(R.drawable.icon, getString(R.string.app_name), System.currentTimeMillis());
	    Notification notification = new Notification.Builder(getApplicationContext())
	        .setContentTitle("CT-Hack")
	        .setContentText("Currently Background Running...")
	        .setSmallIcon(R.drawable.ic_launcher)
//	        .setLargeIcon(aBitmap)
	        .addAction(R.drawable.ic_launcher, "Re-connect ChinaNet", contextIntent)
	        .build();
	    notification.flags = Notification.FLAG_ONGOING_EVENT; // 设置常驻 Flag
//	    notification.setLatestEventInfo(getApplicationContext(), getString(R.string.app_name), getString(R.string.information), contextIntent);
	    notificationManager.notify(R.string.app_name, notification);
	}

	private void cancelNotification() {
	    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    notificationManager.cancel(R.string.app_name);
	}
	
	@SuppressLint("HandlerLeak")
	public final Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			String message=(String)msg.obj;
			console.setText(console.getText()+message+"\n");
		}
	};

	public Thread main = new Thread(new Runnable() {
	    @Override
        public void run() {

	        String a = "--> 正在获取网关地址，请稍候...\n";
        	
            Message message = handler.obtainMessage();
            message.obj=a;
            handler.sendMessage(message);
            
	        try {
	            HttpRequester request = new HttpRequester();
	            HttpRequester addGood = new HttpRequester();
	            HttpRequester openCart = new HttpRequester();
	            HttpRequester reqAc = new HttpRequester();
	            
	            Map<String,String> map1 = new HashMap<String,String>();
	            Map<String,String> map2 = new HashMap<String,String>();
	            Map<String,String> map3 = new HashMap<String,String>();
	            Map<String,String> map4 = new HashMap<String,String>();
	            Map<String,String> map5 = new HashMap<String,String>();
	            
	            map1.put("method","addGood");
	            map1.put("confirm", "yes");
	            map1.put("cardId", "1");
	            map1.put("type", "1");
	            map1.put("count", "1");
	            
	            map2.put("method", "list");
	            
	            map3.put("method", "buy");
	            map3.put("confirm", "yes");
	            map3.put("shopCartFlag", "shopCart");
	            map3.put("cardType", "1");
	            map3.put("cardPayType", "yi");
	            map3.put("user_phone", "");
	            map3.put("smsVerifyCode", "");
	            map3.put("isBenJi", "no");
	            
	            map4.put("method", "get10mCard");
	            
	            //获取登陆地址
	            HttpURLConnection connection =
	            		(HttpURLConnection)new URL("http://www.baidu.com")
	            .openConnection();
	            connection.setRequestProperty("User-Agent", "CDMA+WLAN");
	            connection.connect();
	            int status_code = connection.getResponseCode();
	            if(status_code==302)
	            {
	            	String location = connection.getHeaderField("Location");
		            
	            	Message message1 = handler.obtainMessage();
	                message1.obj="获取到的网关地址："+location+"\n";
	                handler.sendMessage(message1);
	                
	                //获取登陆地址
		            HttpURLConnection redConnection =
		            		(HttpURLConnection)new URL(location)
		            .openConnection();
		            redConnection.setRequestProperty("User-Agent", "CDMA+WLAN");
		            //HttpRespons hr2 = 
		            request.makeContent(redConnection.getHeaderField("Location"), redConnection); 
//		            System.out.println(hr2.getContent());

		            connection.disconnect();

		            HttpURLConnection loginConnection =
		            		(HttpURLConnection)new URL("http://wifi.189.cn/service/index.jsp")
		            .openConnection();
		            loginConnection.setRequestMethod("GET");
		            loginConnection.setRequestProperty("user-agent", "CDMA+WLAN");
		            loginConnection.setUseCaches(false);//不知这句是否有用
		    		loginConnection.connect();
			    		Message message2 = handler.obtainMessage();
		                message2.obj="开始模拟购买时长卡\n";
		                handler.sendMessage(message2);
			            String cookie = (loginConnection.getHeaderField("Set-Cookie")).substring(0,46);
			            Message message3 = handler.obtainMessage();
			            message3.obj="获得的 cookie："+cookie+"\n";
		                handler.sendMessage(message3);
		            loginConnection.disconnect();

		            Message message4 = handler.obtainMessage();
		            message4.obj="开始模拟向购物车添加物品...\n";
	                handler.sendMessage(message4);
		            addGood.sendGet("http://wifi.189.cn/service/cart.do",map1,cookie);
		            Message message5 = handler.obtainMessage();
		            message5.obj="打开购物车，使请求有效化...\n";
	                handler.sendMessage(message5);
		            openCart.sendGet("http://wifi.189.cn/service/cart.do",map2,cookie);

		            //随机生成手机号
		            Random random = new Random();
		            String phone_num = "131";
		            for(int i=1;i<=8;i++)
		            	phone_num = phone_num + String.valueOf(random.nextInt(10));
		            
		            map3.put("phone", phone_num);
		            map3.put("phone_1", phone_num);
		            Message message6 = handler.obtainMessage();
		            message6.obj="开始获取订单号，手机号："+phone_num+"...\n";
	                handler.sendMessage(message6);
		            HttpRespons data = openCart.sendGet("http://wifi.189.cn/service/user.do",map3,cookie);
		            
		            String orderNum = data.getContent().substring(2,18);
		            
		            Message message7 = handler.obtainMessage();
		            message7.obj="得到的订单号："+orderNum+"\n";
	                handler.sendMessage(message7);

	                Message message8 = handler.obtainMessage();
		            message8.obj="开始获取账号密码...\n";
	                handler.sendMessage(message8);
		            map4.put("orderId", orderNum);
		            
		            // 不带 cookie 以便多次获得账号密码
		            HttpRespons key = reqAc.sendGet("http://wifi.189.cn/clientApi.do",map4);
		            String receData = key.getContent();
		            Message message9 = handler.obtainMessage();
		            message9.obj="收到的数据："+receData;
	                handler.sendMessage(message9);
		            acc = receData.substring(11, 23);
	                pss = receData.substring(24);
	                //
//	                t.t("密码" + pss);

		            Message message10 = handler.obtainMessage();
		            message10.obj="开始登录...\n";
	                handler.sendMessage(message10);
		            map5.put("UserName", acc+"@wlan.sh.chntel.com");
		            map5.put("Password", pss);
		            
//	                Timer timer = new Timer();
//	                timer.schedule(new PretendLogin(), 0);
////	                t.t(loginMsg);
//	                timer.wait(5000);
	                Thread.sleep(2000);
	                
	                
	                
	                URLConnection login;
	        		try {
	        			//登陆地址
	        			login = new URL("https://wlan.ct10000.com/portal/wispr.login?NasType=Huawei-NasName=BJ-JA-SR-1.M.ME60").openConnection();

	        			//POST方式请求，带UA
	        			login.setRequestProperty("user-agent", "CDMA+WLAN");
	        			login.setRequestProperty("method", "POST");
	        			login.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	        			
	        			//POST请求必须设置这两项以启用输入输出
	        			login.setDoOutput(true);
	        			login.setDoInput(true);
	        	        
	        			//参数
	        			StringBuffer param = new StringBuffer();

	        			//用户名密码
	        			param.append("&");
	        			param.append("UserName").append("=").append(MainActivity.acc+"@wlan.sh.chntel.com");//必须加全国用户后缀
	        			param.append("&");
	        			param.append("Password").append("=").append(MainActivity.pss);

	        			//请求并关闭
	        			login.getOutputStream().write(param.toString().getBytes());
	        			login.getOutputStream().flush();
	        			login.getOutputStream().close();

//	        			MainActivity.loginMsg = "登陆信息：" + login;// + "\n" + login.getContent();
//	        			//DebugInfo
//		            	Message message11 = handler.obtainMessage();
//			            message11.obj=login.getContent().toString();
//		                handler.sendMessage(message11);
	        			
	        		} catch (MalformedURLException e) {
	        			// TODO Auto-generated catch block
	        			MainActivity.loginMsg = "登陆时发生错误：" + e.getMessage();
//	        			e.printStackTrace();
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			MainActivity.loginMsg = "登陆时发生错误：" + e.getMessage();
//	        			e.printStackTrace();
	        		}
	                
	                
					
	        		
	        		
	                //测试登陆是否成功
		            HttpURLConnection test =
		            		(HttpURLConnection)new URL("http://www.baidu.com")
		            .openConnection();
		            test.connect();
		            
		            if(test.getResponseCode()==200)
		            {
		            	Message message11 = handler.obtainMessage();
			            message11.obj="登录成功！九分半后开始切换账号";
		                handler.sendMessage(message11);
//		                t.t("登录成功！九分半后开始切换账号");
		                
		                //九分半
//		                timer.wait(570000);
		                handler.postDelayed(main, 570000);

//		                //重新启动自己
//		                main.start();
//		                handler.removeCallbacks(main);
////		                main.start();
		            }
		            else{
		            	Message message12 = handler.obtainMessage();
			            message12.obj="登录失败，一秒后再次尝试登录...\n";
		                handler.sendMessage(message12);

		                Thread.sleep(2000);
		                
		                //第二次登陆
//		                timer.schedule(new PretendLogin(), 5000);
//		                t.t(loginMsg);
		                
		                URLConnection login2;
		        		try {
		        			//登陆地址
		        			login2 = new URL("https://wlan.ct10000.com/portal/wispr.login?NasType=Huawei-NasName=BJ-JA-SR-1.M.ME60").openConnection();

		        			//POST方式请求，带UA
		        			login2.setRequestProperty("user-agent", "CDMA+WLAN");
		        			login2.setRequestProperty("method", "POST");
		        			login2.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		        			
		        			//POST请求必须设置这两项以启用输入输出
		        			login2.setDoOutput(true);
		        			login2.setDoInput(true);
		        	        
		        			//参数
		        			StringBuffer param2 = new StringBuffer();

		        			//用户名密码
		        			param2.append("&");
		        			param2.append("UserName").append("=").append(MainActivity.acc+"@wlan.sh.chntel.com");//必须加全国用户后缀
		        			param2.append("&");
		        			param2.append("Password").append("=").append(MainActivity.pss);

		        			//请求并关闭
		        			login2.getOutputStream().write(param2.toString().getBytes());
		        			login2.getOutputStream().flush();
		        			login2.getOutputStream().close();

		        			//debug
//			            	Message message11 = handler.obtainMessage();
//				            message11.obj=login2.getContent().toString();
//			                handler.sendMessage(message11);
		        			
		        		} catch (MalformedURLException e) {
		        			// TODO Auto-generated catch block
		        			MainActivity.loginMsg = "登陆时发生错误：" + e.getMessage();
//		        			e.printStackTrace();
		        		} catch (IOException e) {
		        			// TODO Auto-generated catch block
		        			MainActivity.loginMsg = "登陆时发生错误：" + e.getMessage();
//		        			e.printStackTrace();
		        		}
		        		

		        		
		                
		                //测试第二次登陆是否成功
			            HttpURLConnection testC =
			            		(HttpURLConnection)new URL("http://www.baidu.com")
			            .openConnection();
			            testC.connect();
			            if(testC.getResponseCode()==200)
			            {
			            	Message message11 = handler.obtainMessage();
				            message11.obj="登录成功！九分半后开始切换账号";
			                handler.sendMessage(message11);
//			                t.t("登录成功！九分半后开始切换账号");
			                handler.postDelayed(main, 570000);
			            }else{
			            	Message message11 = handler.obtainMessage();
				            message11.obj="第二次登陆失败，请手动重启";
			                handler.sendMessage(message11);
//			                t.t("第二次登陆失败，请手动重启");
			            }
		            }
		            
		            //关闭timer
//		            timer.cancel();
		            
	            }else if(status_code==200){
	            	Message message13 = handler.obtainMessage();
		            message13.obj="您现在已连接到网络，无需破解\n";
	                handler.sendMessage(message13);
//	                t.t("您现在已连接到网络，无需破解");
		            connection.disconnect();//
	            }else{
	            	Message message14 = handler.obtainMessage();
		            message14.obj="没有获得网关地址，请检查网络连接";
	                handler.sendMessage(message14);
		            connection.disconnect();//
	            }

	        } catch (Exception e) {
	        	Message messagee = handler.obtainMessage();
	            messagee.obj="发生错误: " + e.getMessage()
	            		+ "\n请获取屏幕截图并发送至lisr1228@163.com\n帮助我改进此应用 谢谢！";
                handler.sendMessage(messagee);
	            e.printStackTrace();//
	        }
//	        handler.sendEmptyMessage(0);
	}});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		console = (TextView) findViewById(R.id.console);

		String a = "ChinaNet Portal Hacking v0.4.0 by Dolphin @BUCT_SNC_SYS.\n";
	    	a += "Copyright 2014 Dolphin Wood.\n";
	    	a += "Licensed under http://opensource.org/licenses/MIT\n\n";
	    	a += "Designed and built with all the love in the world.\n\n";
	    	a += "Everything will be done automatically :)\n\n";
	    	a += "Refactored with JAVA by Nyx @BUCT_SNC_DEV.\n";
	    	a += "进程守护已启动！\n";
	    	
	    console.setText(a);
	    
	    Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
	            cancelNotification();
            	finish();
            }
        });

    	setNotification();
    	
	    main.start();
	}
}
