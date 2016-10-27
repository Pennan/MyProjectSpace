package com.np.a07realmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.np.a07realmdemo.entitiy.Student;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReamTag";
    private Button button_add, button_query_all, button_query_single, button_delete, button_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_add = (Button) findViewById(R.id.button_add);
        button_query_all = (Button) findViewById(R.id.button_query_all);
        button_query_single = (Button) findViewById(R.id.button_query_single);
        button_update = (Button) findViewById(R.id.button_update);
        button_delete = (Button) findViewById(R.id.button_delete);

        button_add.setOnClickListener(this);
        button_query_all.setOnClickListener(this);
        button_query_single.setOnClickListener(this);
        button_update.setOnClickListener(this);
        button_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
//                addDataToRealm();
//                copyDataToRealm();
                useTransactionToAddData();
                break;
            case R.id.button_query_all:
                queryAllDataFromRealm();
                break;
            case R.id.button_query_single:
                String id = "1";
                querySingleDataFromRealm(id);
                break;
            case R.id.button_delete:
                deleteDataToRealm();
                break;
            case R.id.button_update:
                String id3 = "100";
                updateDataToRealm(id3);
                break;
        }
    }

    /** 删除数据 **/
    private void deleteDataToRealm() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Student> students = realm.where(Student.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // 删除一条数据
                Student student = students.get(2);
                student.deleteFromRealm();
                // 删除第三条数据
//                students.deleteFromRealm(3);
                // 删除第一条数据
//                students.deleteFirstFromRealm();
                // 删除最后一条数据
//                students.deleteLastFromRealm();
                // 删除所有数据
                students.deleteAllFromRealm();
            }
        });
    }

    /** 通过条件修改数据 **/
    private void updateDataToRealm(String id) {
        Realm realm = Realm.getDefaultInstance();
        Student student = realm.where(Student.class).equalTo("id", id).findFirst();

        realm.beginTransaction();
        student.setAge(20);
        Log.i(TAG, "student = " + student);
        realm.commitTransaction();
    }

    /** 条件查询数据 **/
    private void querySingleDataFromRealm(String id) {
        Realm realm = Realm.getDefaultInstance();
        Student student = realm.where(Student.class).equalTo("id", id).findFirst();
        Log.i(TAG, "student = " + student);
    }

    /** 查询 Student 表中所有的数据 **/
    private void queryAllDataFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Student> students = realm.where(Student.class).findAll();
        // 增序排列
        students = students.sort("id");
        // 降序排列
//        students = students.sort("id", Sort.DESCENDING);
        List<Student> studentList = realm.copyFromRealm(students);
        for (Student student : studentList){
            Log.i(TAG, "student = " + student);
        }
    }

    /** 事物块操作：添加数据 **/
    private void useTransactionToAddData() {
        Realm realm = Realm.getDefaultInstance();

        final Student student = new Student("Marry", 28);
        student.setMan(false);
        student.setId(UUID.randomUUID().toString()); // id 必须不同，要保持表中 key 的唯一性

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(student);
            }
        });
    }

    /** 事物操作：复制一个对象到 Realm 数据库 **/
    private void copyDataToRealm() {
        Realm realm = Realm.getDefaultInstance();

        Student student = new Student("Jake", 18);
        student.setMan(false);

        realm.beginTransaction();
        realm.copyToRealm(student);
        realm.commitTransaction();
    }

    /** 事物操作：新建一个对象,并进行存储 **/
    private void addDataToRealm() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        // 如果 实体类 设置了 PrimaryKey,则需要使用 createObject(clazz, object) 方法,否则会报错.
        Student student = realm.createObject(Student.class, "1");
        student.setName("Pennan");
        student.setAge(18);
        student.setMan(true);

        realm.commitTransaction();
    }
}
