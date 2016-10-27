package com.np.aidldemo.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.np.aidldemo.R;

public class MessengerActivity extends AppCompatActivity {
    private Messenger mMessenger;
    private Messenger clientMessenger = new Messenger(new ClientHandler());
    private static class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x02: // 接受服务端的回复消息
                    Log.i("MessengerActivity", "receive message from server:" +
                        msg.getData().getString("msg"));
                    break;
            }
        }
    }
    public void sendMsgToServer(View view) {
        Message message = Message.obtain(null, 0x01);
        Bundle bundle = new Bundle();
        bundle.putString("msg", "hello, this is client");
        message.setData(bundle);
        // 传递给 服务端的 Messenger，以便服务端回复消息
        message.replyTo = clientMessenger;
        try {
            mMessenger.send(message); // 发送消息到服务端
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };
    public void bindMessengerServiceClick(View view) {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
