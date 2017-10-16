package com.bugua.culweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-10-14.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public Condition condition;

    @SerializedName("wind")
    public Wind wind;

    public class Condition {

        @SerializedName("txt")
        public String info;

    }

    public class Wind {
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String intension;
    }
}
