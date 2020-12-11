package com.javarush.task.task27.task2712;

import java.util.ArrayList;
import java.util.List;

public class RandomOrderGeneratorTask implements Runnable {
    private List<Tablet> tablets;
    private int interval;

    public RandomOrderGeneratorTask(List<Tablet> tablets, int interval) {
        this.tablets = tablets;
        this.interval = interval;
        //interval при создании приравнять к Restaurant.ORDER_CREATING_INTERVAL;
    }

    @Override
    public void run() {
        if(tablets != null && tablets.size() > 0){
            while(true) {
                int size = tablets.size();
                int r = (int) ((Math.random()) * size);
                Tablet tablet = tablets.get(r);

                tablet.createTestOrder();

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    //System.out.println("ШЫШЕЛ");
                    return;
                }
            }
        }
    }
}
