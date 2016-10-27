package com.np.aidldemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.np.aidldemo.R;
import com.np.aidldemo.entitiy.People;
import com.np.aidldemo.entitiy.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity {

    public static final String TAG = SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        User user = getIntent().getParcelableExtra("User");
        Toast.makeText(SecondActivity.this, "User:" + user, Toast.LENGTH_SHORT).show();
    }

    public void getUserClick(View view) {
        getUser();
    }

    private void getUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cachePath = SecondActivity.this.getExternalCacheDir() + "/people.txt";
                File file = new File(cachePath);
                try {
                    ObjectInputStream inputStream = new
                            ObjectInputStream(new FileInputStream(file));
                    try {
                        People people = (People) inputStream.readObject();
                        Log.i(TAG, "SecondActivity:People=" + people);
                    } catch (ClassNotFoundException e) {
                        Toast.makeText(SecondActivity.this, "该文件不存在", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
