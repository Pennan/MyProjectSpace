package com.np.aidldemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.np.aidldemo.R;
import com.np.aidldemo.entitiy.People;
import com.np.aidldemo.entitiy.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void saveUserClick(View view) {
        saveUser();
    }

    public void startSecondActivityClick(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        User user = new User(1, "Cool", true);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    private void saveUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File cacheDir = FirstActivity.this.getExternalCacheDir();
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs();
                    }
                    People people = new People("Pennan", "男");
                    String cachePath = FirstActivity.this.getExternalCacheDir() + "/people.txt";
                    File file = new File(cachePath);
                    ObjectOutputStream outputStream = new
                            ObjectOutputStream(new FileOutputStream(file));
                    outputStream.writeObject(people);
                    Log.i("SecondActivity", "FirstActivity:People=" + people);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
