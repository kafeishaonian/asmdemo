package com.example.plugin.statistic;

public class Demo {



    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.abdss();
    }


    private void abdss() {
        Timesutils entity = new Timesutils();
        long slotIndex = System.currentTimeMillis();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        slotIndex = System.currentTimeMillis() - slotIndex;
        if (slotIndex > entity.getTime()) {
            System.out.println("--> execution time : (" + slotIndex + "ms)");
        }
    }
}