package com.bugua.culweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-10-14.
 */

public class Suggestion {
    public Air air;
    @SerializedName("comf")
    public Comfortlevel comfortlevel;
    @SerializedName("cw")
    public CarWash carWash;
    @SerializedName("drsg")
    public DressTips dressTips;
    public Flu flu;
    public Sport sport;
    @SerializedName("trav")
    public Travel travel;
    @SerializedName("uv")
    public UvRadiation uvRadiation;
    public class Air{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public class Comfortlevel{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public class CarWash{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public class DressTips{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public class Flu{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public  class Sport{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public class Travel{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
    public class UvRadiation{
        @SerializedName("brf")
        public String tips;
        @SerializedName("txt")
        public String context;
    }
}
