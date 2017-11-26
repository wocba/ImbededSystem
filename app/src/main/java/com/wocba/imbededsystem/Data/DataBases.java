package com.wocba.imbededsystem.Data;

import android.provider.BaseColumns;

/**
 * Created by jinwo on 2017-11-10.
 */

public class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String NAME = "name";
        public static final String IMAGE = "image";
        public static final String LATI = "lati";
        public static final String LONGI = "longi";
        public static final String CONTENT = "content";
        public static final String _TABLENAME = "data";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +NAME+" text not null , "
                        +IMAGE+" text not null , "
                        +LATI+" text not null, " + LONGI + " text not null, " + CONTENT + " text not null);";
    }
}
