package com.np.aidldemo.entitiy;

import java.io.Serializable;

/**
 * <B>Des</B>
 *
 * @author ningpan 2016/10/3 11:04
 */
public class People implements Serializable {

    private static final long serialVersionUID = 1736705311199022229L;

    public String name;
    public String sex;

    public People(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
