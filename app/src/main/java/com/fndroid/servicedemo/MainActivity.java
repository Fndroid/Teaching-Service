package com.fndroid.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";
	private Intent service;
	private ServiceConnection conn;
	private MyService.MyBinder binder;
	private TextView tv;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				tv.setText("Nice to meet you");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv);
	}

	public void startService(View view) {
		service = new Intent(this, MyService.class);
		startService(service);
	}

	public void stopService(View view) {
		stopService(service);
	}

	// “绑定服务”按钮的点击事件
	public void bindService(View view) {
		service = new Intent(this, MyService.class);
		conn = new MyServicConnection();
		bindService(service, conn, BIND_AUTO_CREATE);
	}

	// “解绑服务”按钮的点击事件
	public void unbindService(View view) {
		unbindService(conn);
	}

	// “执行服务中的方法”按钮的点击事件
	public void callMethod(View view) {
		binder.callMethod();
	}

	public class MyServicConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MyService.MyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {}
	}

	public void subThread(View view) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	// 按钮点击事件
	public void asyncTask(View view) {
		AsyncTask<Void, Integer, Boolean> asyncTask = new AsyncTask<Void, Integer, Boolean>() {
			private View view;
			private ProgressBar mProgressBar;
			private AlertDialog mAlertDialog;

			@Override
			protected Boolean doInBackground(Void... params) {
				Log.d(TAG, "doInBackground: 开始下载");
				for (int i = 0; i < 100; i++) {
					try {
						Thread.sleep(50);
						publishProgress(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				view = LayoutInflater.from(MainActivity.this).inflate(R.layout.progress, null);
				mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
				builder.setView(view);
				mAlertDialog = builder.create();
				mAlertDialog.show();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				mProgressBar.setProgress(values[0]);
			}

			@Override
			protected void onPostExecute(Boolean aBoolean) {
				super.onPostExecute(aBoolean);
				if (aBoolean) {
					Log.d(TAG, "onPostExecute: 下载成功");
				} else {
					Log.d(TAG, "onPostExecute: 下载失败");
				}
				mAlertDialog.dismiss();
			}
		};
		asyncTask.execute();
	}
}
