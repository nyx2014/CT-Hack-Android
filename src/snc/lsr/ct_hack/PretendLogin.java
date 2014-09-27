package snc.lsr.ct_hack;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

public class PretendLogin extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
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

			MainActivity.loginMsg = "登陆信息：" + login;// + "\n" + login.getContent();
//			//DebugInfo
//			System.out.println(login);
//			System.out.println(login.getContent());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			MainActivity.loginMsg = "登陆时发生错误：" + e.getMessage();
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MainActivity.loginMsg = "登陆时发生错误：" + e.getMessage();
//			e.printStackTrace();
		}
	}

}
