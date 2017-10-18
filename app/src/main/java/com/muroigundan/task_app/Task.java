package com.muroigundan.task_app;

import java.sql.Date;
import io.realm.RealmObject;

/**
 * Created by local-user on 2017/10/18.
 */

public class Task extends RealmObject{
    //@PrimaryKey
    //private int id;
    private String subject; //件名
    private Date deadline; //〆切
    private int importance; //重要度
    private int remarks; //備考
}
