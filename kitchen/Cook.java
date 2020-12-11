package com.javarush.task.task27.task2712.kitchen;
import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable{
    private LinkedBlockingQueue<Order> queue;
    private String name;
    private boolean busy;

    public Cook(String name) {
        this.name = name;
    }

    //Перенеси логику из трэда внутри конструктора Test в метод run класса Cook.
    @Override
    public void run() {
            try {
                while(true){
                        if(!queue.isEmpty()){
                            if(!this.isBusy()){
                                startCookingOrder(queue.take());
                            }
                        }
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

//Observable o == Tablet; Object arg = Order;
//    @Override
//    public void update(Observable o, Object arg) {
//        ConsoleHelper.writeMessage("Start cooking - " + arg + ", cooking time " + ((Order)arg).getTotalCookingTime() + "min");
//        setChanged();
//        notifyObservers(arg);
//        //4. Зарегистрируй событие для повара во время приготовления еды.
//        //Добавь геттер для поля dishes в класс Order, используйте его при создании события.
//        //3. Повар во время приготовления еды должен генерировать соответствующее событие.
//        //public CookedOrderEventDataRow(String tabletName, String cookName, int cookingTimeSeconds, List<Dish> cookingDishs)
//        StatisticManager.getInstance().register(new CookedOrderEventDataRow(o.toString(), getName(), ((Order) arg).getTotalCookingTime()*60, ((Order) arg).getDishes()));
//    }

    //Закомментил свой update() выше. Это чужой!
//    @Override
//    public void update(Observable o, Object arg) {
//        Order order = (Order) arg;
//
//        ConsoleHelper.writeMessage("Start cooking - " + arg + ", cooking time " + order.getTotalCookingTime() + "min");
//
//        StatisticManager.getInstance().register(new CookedOrderEventDataRow(
//                o.toString(),
//                this.name,
//                order.getTotalCookingTime()*60,
//                order.getDishes()));
//
//        setChanged();
//        notifyObservers(arg);
//    }

    public void startCookingOrder(Order order){
        busy = true;
        ConsoleHelper.writeMessage("Start cooking - " + order + ", cooking time " + order.getTotalCookingTime() + "min");

        StatisticManager.getInstance().register(new CookedOrderEventDataRow(
                order.getTablet().toString(), //tabletName
                this.name,
                order.getTotalCookingTime()*60,
                order.getDishes()));

        //2) Если 3 пункт не проходит - посмотрите ConsoleHelper
        // (вот никогда бы не догадался).
        // Reader должен создаваться на уровне класса, а не в методе.
        try {
            Thread.sleep(order.getTotalCookingTime()*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers(order);
        busy = false;
    }

    public String getName() {
        return name;
    }

    public boolean isBusy(){
        return busy;
    }

    @Override
    public String toString() {
        return name;
    }


}
