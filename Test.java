package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Test implements Observer {
//    private static volatile Test instance;
    //private final LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>(); //22-> переносим в ресторан

    //5. В конструкторе Test создай и запусти демон-трэд. Логика метода run:
    //каждые 10 миллисекунд проверять очередь.
    // Если в очереди есть заказы, то найти свободных поваров и передать им заказы
    // (метод startCookingOrder),
    // если нет свободных поваров или нет заказов в очереди, то ждать дальше.
    public Test(){
//        Thread thread = new Thread(() -> {
//            try {
//                while(true){
//                    Set<Cook> cooks = StatisticManager.getInstance().getCooks();
//                    for(Cook cook : cooks){
//                        if(!orderQueue.isEmpty()){
//                            if(!cook.isBusy()){
//                                cook.startCookingOrder(orderQueue.take());
//                            }
//                        }
//                    }
//                    Thread.sleep(10);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
    }

//    private Test(){
//
//    }

//    public static Test getInstance(){
//        Test localInstance = instance;
//        if(localInstance == null){
//            synchronized (Test.class){
//                localInstance = instance;
//                if(localInstance == null){
//                    instance = localInstance = new Test();
//                }
//            }
//        }
//        return instance;
//    }

    @Override          //Планшет     Заказ
    public void update(Observable o, Object arg) {
        //Tablet tablet = (Tablet)o;
        Order order = (Order)arg;
        boolean offerSucceeded = false;

        //orderQueue.offer(order);

    }
}
