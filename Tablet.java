package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.NoAvailableVideoEventDataRow;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet{
    //5. У нас все завязано на работу с консолью. Однако, при возникновении исключений, наше приложение умрет.
    //Чтобы узнать причину - добавим в Tablet статическое поле logger типа java.util.logging.Logger, инициализированное именем класса (Logger.getLogger(Tablet.class.getName())).
    private static Logger logger = Logger.getLogger(Tablet.class.getName());
    private final int number; //это номер планшета, чтобы можно было однозначно установить, откуда поступил заказ.
    private LinkedBlockingQueue<Order> queue;

    public Tablet(int number) {
        this.number = number;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public int getNumber() {
        return number;
    }

    //6. В методе createOrder класса Tablet обработаем исключения ввода-вывода.
    //Запишем в лог "Console is unavailable.". Уровень лога - SEVERE - это самый серьезный уровень, мы не можем работать.
    //Также в методе createOrder класса Tablet должен быть создан новый заказ.
    //5. В методе createOrder класса Tablet должен быть вызван метод setChanged.
    //6. В методе createOrder класса Tablet должен быть вызван метод notifyObservers с текущим заказом в качестве параметра.
    //5. В методе createOrder класса Tablet должен быть создан новый AdvertisementManager
    // и у него должен быть вызван метод processVideos.

    public Order createOrder() {
        Order order = null;
        try {
            order = new Order(this);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } finally {
            if (!order.isEmpty()) {
                //5. В Tablet часть логики, которая уведомляет Observer-а,
                // замени на такую, которая добавляет заказ в очередь.
                try {
                    queue.put(order);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                setChanged();
//                notifyObservers(order);
                AdvertisementManager advertisementManager = new AdvertisementManager(order.getTotalCookingTime() * 60);
                try {
                    advertisementManager.processVideos();
                } catch (NoVideoAvailableException e) {
                    StatisticManager.getInstance().register(new NoAvailableVideoEventDataRow(order.getTotalCookingTime() * 60));
                    logger.log(Level.INFO, "No video is available for the order " + order.toString());
                }
            }
        }
        return order;
    }

    //Сейчас заказ создается в методе createOrder в классе Tablet.
    //В классе Tablet создай метод void createTestOrder() с похожей функциональностью,
    // который будет случайным образом генерировать заказ со случайными блюдами
    // не общаясь с реальным человеком.
    //Все необходимые данные передай в конструкторе.
    public void createTestOrder() {
        //д) вместо создания объекта Order в методе createTestOrder() класса Tablet,
        // создавай объект класса TestOrder.
        //Весь другой функционал метода createTestOrder оставь прежним.
        TestOrder order = null;
        try {
            order = new TestOrder(this);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } finally {
            if (!order.isEmpty()) {
                //5. В Tablet часть логики, которая уведомляет Observer-а,
                // замени на такую, которая добавляет заказ в очередь.
                try {
                    queue.put(order);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                setChanged();
//                notifyObservers(order);
                AdvertisementManager advertisementManager = new AdvertisementManager(order.getTotalCookingTime() * 60);
                try {
                    advertisementManager.processVideos();
                } catch (NoVideoAvailableException e) {
                    StatisticManager.getInstance().register(new NoAvailableVideoEventDataRow(order.getTotalCookingTime() * 60));
                    logger.log(Level.INFO, "No video is available for the order " + order.toString());
                }
            }
        }
        //return order;
    }


    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
