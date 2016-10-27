package com.np.a07realmdemo.entitiy;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * <B>Des</B>
 *
 * @author ningpan 2016/10/27 10:19
 */
public class Teacher extends RealmObject {

    private String name;
    private RealmList<Student> mStudents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Student> getStudents() {
        return mStudents;
    }

    public void setStudents(RealmList<Student> students) {
        mStudents = students;
    }

    public Teacher() {
    }

    public Teacher(String name, RealmList<Student> students) {
        this.name = name;
        mStudents = students;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", mStudents=" + mStudents +
                '}';
    }
}
