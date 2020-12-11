package com.javarush.task.task27.task2712;

//директор мог посмотреть:
//1. какую сумму заработали на рекламе, сгруппировать по дням;
//2. загрузка (рабочее время) повара, сгруппировать по дням;
//3. список активных роликов и оставшееся количество показов по каждому;
//4. список неактивных роликов (с оставшемся количеством показов равным нулю).
//
//Для каждого пункта добавим соответствующий метод в StatisticManager.
//Директор будет вызывать метод, StatisticManager будет делать различные подсчеты.
//Но директор должен из какого-то места вызвать эти методы. Дадим ему планшет, но с другим ПО.
//Для этого создадим класс DirectorTablet, в котором будут дружелюбный интерфейс и возможность обращения к статистике.

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DirectorTablet {

    //какую сумму заработали на рекламе, сгруппировать по дням;
    //Используя метод из предыдущего пункта вывести в консоль в убывающем порядке даты и суммы.
    //Для каждой даты из хранилища событий, для которой есть показанная реклама, должна выводится сумма прибыли за показы рекламы для этой даты.
    //В конце вывести слово Total и общую сумму.
    //
    //Пример:
    //14-Jul-2013 - 2.50
    //13-Jul-2013 - 1.02
    //12-Jul-2013 - 543.98
    //Total - 547.50

//Это решение с https://javarush.ru/help/2334
    public void printAdvertisementProfit() {
        TreeMap<Date, Double> adProfitsByDay = StatisticManager.getInstance().getAdProfitByDay();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        double total = 0.0;
        for (Map.Entry<Date, Double> entry : adProfitsByDay.entrySet()) {
            Date date = entry.getKey();
            double amount = entry.getValue();
            total += amount;
            if (amount > 0) {
                ConsoleHelper.writeMessage(String.format("%s - %.2f", dateFormat.format(date), amount));
            }
        }
        ConsoleHelper.writeMessage(String.format("Total - %.2f", total));
    }


    //это моё решение:
//    public void printAdvertisementProfit(){
//        NavigableMap<Date, Double> map = (NavigableMap<Date, Double>)StatisticManager.getInstance().getStatisticForShownAdvertisement();
//        map.descendingMap();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//
//        List<String> list = map.descendingMap().entrySet().stream().map(k -> sdf.format(k.getKey()) + " - " + k.getValue()).collect(Collectors.toList());
//        double totalAmount = map.entrySet().stream().map(k -> k.getValue()).mapToDouble(k -> k).sum();
//
//
//        for( String s : list){
//            ConsoleHelper.writeMessage(s);
//        }
//        ConsoleHelper.writeMessage("Total - " + totalAmount);
//    }

//это решение с https://github.com/Polurival/JRHW/blob/master/src/com/javarush/test/level27/lesson15/big01/DirectorTablet.java
//    public void printAdvertisementProfit()
//    {
//        Map<String, Double> map = StatisticManager.getInstance().getStatisticForShownAdvertisement();
//        double totalAmount = 0;
//
//        for (Map.Entry<String, Double> entry : map.entrySet())
//        {
//            totalAmount += entry.getValue();
//            System.out.println(entry.getKey() + " - " + String.format("%.2f", entry.getValue()));
//        }
//        System.out.println(String.format("Total - %.2f", totalAmount));
//    }


    //загрузка (рабочее время) повара, сгруппировать по дням;

    public void printCookWorkloading()
    {
        Map<String, Map<String, Integer>> map = StatisticManager.getInstance().getStatisticForCooks();

        for (Map.Entry<String, Map<String, Integer>> entry1 : map.entrySet())
        {
            System.out.println(entry1.getKey());
            for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet())
            {
                System.out.println(entry2.getKey() + " - " + entry2.getValue() + " min");
            }
        }
    }

//это реализация с https://javarush.ru/help/58540
//    public void printCookWorkloading() {
//        Locale.setDefault(Locale.ENGLISH);
//        Map<Date, Map<String, List<CookedOrderEventDataRow>>> cookOnTime = StatisticManager.getInstance().getCookStatistic();
//        for (Map.Entry<Date, Map<String, List<CookedOrderEventDataRow>>> pair : cookOnTime.entrySet()) {
//            ConsoleHelper.writeMessage(String.format("%1$td-%1$tb-%1$tY", pair.getKey()));
//            for (Map.Entry<String, List<CookedOrderEventDataRow>> pairName : pair.getValue().entrySet()) {
//                Integer sum = pairName.getValue().stream().mapToInt(o -> o.getTime()).sum();
//                ConsoleHelper.writeMessage(pairName.getKey() + " - " + sum + " min");
//            }
//            //ConsoleHelper.writeMessage("");
//        }
//    }
    //список активных роликов и оставшееся количество показов по каждому;
    //4. Реализуй логику методов printActiveVideoSet и printArchivedVideoSet в классе DirectorTablet.
    //Используй методы/метод, созданные в предыдущем пункте.
    //Сортировать по имени видео-ролика в алфавитном порядке без учета регистра.
    //Сначала английские, потом русские.
    //
    //Пример вывода для printActiveVideoSet (имя ролика - оставшееся количество показов ролика):
    //First Video - 100
    //Second video - 10
    //Third Video - 2
    //четвертое видео - 4
    public void printActiveVideoSet(){
        List<Advertisement> list = StatisticAdvertisementManager.getInstance().getActiveAdvertisements();
        list.sort(new NameAdvComparator());
        for (Advertisement a: list) {
            ConsoleHelper.writeMessage(a.getName() + " - " + a.getHits());
        }

    }
    //список неактивных роликов (с оставшемся количеством показов равным нулю).
    public void printArchivedVideoSet(){
        List<Advertisement> list = StatisticAdvertisementManager.getInstance().getNoneActiveAdvertisements();
        list.sort(new NameAdvComparator());
        for (Advertisement a: list) {
            ConsoleHelper.writeMessage(a.getName());
        }
    }

    private class NameAdvComparator implements Comparator<Advertisement>{
        @Override
        public int compare(Advertisement o1, Advertisement o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
}
