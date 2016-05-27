package com.fndroid.servicedemo;

		import android.app.Service;
		import android.content.Intent;
		import android.os.Binder;
		import android.os.IBinder;
		import android.support.annotation.Nullable;
		import android.util.Log;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MyService extends Service {
	private static final String TAG = "MyService";
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind() called with: " + "intent = [" + intent + "]");
		return new MyBinder();
	}

	public class MyBinder extends Binder{
		public void callMethod(){
			method();
		}
	}

	private void method(){
		Log.d(TAG, "服务内部的方法被调用了");
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate() called with: " + "");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy() called with: " + "");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() called with: " + "intent = [" + intent + "], flags = [" +
				flags + "], startId = [" + startId + "]");
		return super.onStartCommand(intent, flags, startId);
	}
}
