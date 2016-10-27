package com.np.a07realmdemo.entitiy;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * <B>Des</B>
 *
 * @author ningpan 2016/10/27 10:17
 */
public class Student extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private int age;
    private boolean isMan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMan() {
        return isMan;
    }

    public void setMan(boolean man) {
        isMan = man;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isMan=" + isMan +
                '}';
    }

    public Student() {
    }

    public Student(String id, String name, int age) {

        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Student(String name, int age, boolean isMan) {
        this.name = name;
        this.age = age;
        this.isMan = isMan;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
