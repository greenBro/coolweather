package com.bugua.culweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-10-14.
 */

public class DailyForecast {
    public String date;
    @SerializedName("cond")
    public Condition condition;
    @SerializedName("tmp")
    public Temperature temperature;
    @SerializedName("wind")
    public Wind wind;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public class Condition {
        @SerializedName("txt_d")
        public String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public class Temperature {
        public String max;
        public String min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }

    public class Wind {
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String intension;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getIntension() {
            return intension;
        }

        public void setIntension(String intension) {
            this.intension = intension;
        }
    }
}
