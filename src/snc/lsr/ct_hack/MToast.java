package snc.lsr.ct_hack;

import android.content.Context;
import android.widget.Toast;

public class MToast {
	private Context context;
	private static int time = 1300;

	public MToast(Context mContext){
		context = mContext;
	}
	
	public void t(String msg){
		Toast.makeText(context,"["+ msg+"]", time).show();
	}
	
	public void rt(int rId){
		Toast.makeText(context,rId, time).show();
	}
}
