package com.np.aidldemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    public MessengerService() {
    }
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    // 接受客户端的消息
                    Bundle bundle = msg.getData();
                    String m = bundle.getString("msg");
                    Log.i("MessengerService", "msg:" + m);

                    // 回复客户端
                    Messenger client = msg.replyTo;
                    Message message = Message.obtain(null, 0x02);
                    Bundle b = new Bundle();
                    b.putString("msg", "嗯，您的消息我以收到，稍后回复您。");
                    message.setData(b);
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
