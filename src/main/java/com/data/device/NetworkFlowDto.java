package com.data.device;

public class NetworkFlowDto {
    private int day;
//    private float send;
//    private float receive;
//    private float max;
    private float total;

    public NetworkFlowDto(int day, float total) {
        this.day = day;
        this.total = total;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
