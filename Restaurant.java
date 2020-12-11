package com.javarush.task.task27.task2712;


import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.Waiter;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class Restaurant {
    private final static int ORDER_CREATING_INTERVAL = 100;
    private static final LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {

        Cook firstCook = new Cook("Amigo");
        Cook secondCook = new Cook("Diego");

        //сразу после создания повара, используя созданный сеттер,
        // установи ему константу из п.1 в качестве значения для созданного поля.
        firstCook.setQueue(orderQueue);
        secondCook.setQueue(orderQueue);

        Waiter waiter = new Waiter();

        firstCook.addObserver(waiter);
        secondCook.addObserver(waiter);

        //создаю пустой список планшетов:
        List<Tablet> tablets = new ArrayList<>();
        //заполняю список 5-ю новыми планшетами:
        Stream.iterate(1, i -> i+1).limit(5).forEach(i -> tablets.add(new Tablet(i)));
        //4. В Tablet добавь поле-очередь LinkedBlockingQueue queue,
        // создай сеттер для него и установи ссылку на очередь (п.1) при создании планшета.

        tablets.stream().forEach(t -> t.setQueue(orderQueue));
        //tablets.stream().forEach(t -> t.addObserver(t.getNumber()%2 == 0 ? firstCook : secondCook));

        //назначаю наблюдателей для планшетов:
        //3. Tablet не должен быть Observable. Убери все зависимости.
        //tablets.stream().forEach(t -> t.addObserver(orderManager));

        Thread t = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage(e.getMessage());
        }

        t.interrupt();


        DirectorTablet directorTablet = new DirectorTablet();

        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
    }
}
