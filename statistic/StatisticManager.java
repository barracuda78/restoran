package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticManager {
    private static volatile StatisticManager instance;
    //2. Чтобы менеджер мог получить доступ к хранилищу, нужно в классе StatisticManager создать поле statisticStorage типа StatisticStorage.
    //Инициализируй его экземпляром класса.
    private StatisticStorage statisticStorage = new StatisticStorage();
    //Создай в классе StatisticManager множество (Set) поваров (cooks) и добавь в него повара.
    //6. Из класса StatisticManager удали сет поваров, его геттер и метод register(Cook cook).
    //private Set<Cook> cooks = new HashSet<>();   //------------------> добавил private не знаю зачем!

    private StatisticManager(){

    }

    public static StatisticManager getInstance(){
        StatisticManager localInstance = instance;
        if(localInstance == null){
            synchronized (StatisticManager.class){
                localInstance = instance;
                if(localInstance == null){
                    instance = localInstance = new StatisticManager();
                }
            }
        }
        return instance;
    }

    //6. Метод register класса StatisticManager с одним параметром типа EventDataRow
    // должен регистрировать полученное событие в statisticStorage.
    public void register(EventDataRow data){
        statisticStorage.put(data);
    }

    //зарегистрирует полученного повара.
    //4. В классе StatisticManager должен быть реализован метод register с одним параметром типа Cook, регистрирующий полученного повара в множестве всех поваров (cooks).
//    public void register(Cook cook){
//        cooks.add(cook);
//    }

    //Ресторан(16):
    //2. В StatisticManager создай метод (придумать самостоятельно),
    // который из хранилища достанет все данные, относящиеся к отображению рекламы,
    // и посчитает общую прибыль за каждый день.
    //Дополнительно добавь вспомогательный метод get в класс хранилища,
    //чтобы получить доступ к данным.

    public TreeMap<Date, Double> getAdProfitByDay() {
        List<EventDataRow> videoList = statisticStorage.getStorage().get(EventType.SELECTED_VIDEOS);
        TreeMap<Date, Double> amountPerDay = new TreeMap<>(Collections.reverseOrder());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        // меняем Date  на стринг - чтобы сравнивать дату по дню, а не секундам-минутам!
        for (EventDataRow row : videoList) {
            VideoSelectedEventDataRow videoRow = (VideoSelectedEventDataRow) row;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(videoRow.getDate());
            GregorianCalendar gregorianCalendar = new GregorianCalendar(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            Date date = gregorianCalendar.getTime();

//            String date = dateFormat.format(videoRow.getDate());
//            Long amount = videoRow.getAmount();
            double amount = 1.0 * videoRow.getAmount() / 100;
            if (!amountPerDay.containsKey(date)) {
                amountPerDay.put(date, amount);
            } else {
                amountPerDay.put(date, amountPerDay.get(date) + amount);
            }
        }
        return amountPerDay;
    }

    //второй пункт статистики - загрузка (рабочее время) повара, сгруппировать по дням.
    //В StatisticManager создай метод (придумать самостоятельно), который из хранилища достанет все данные,
    // относящиеся к работе повара, и посчитает общую продолжительность работы для каждого повара отдельно.
    //
    //5. Реализуем логику метода printCookWorkloading в классе DirectorTablet.
    //Используя метод из предыдущего пункта вывести в консоль в убывающем порядке даты, имена поваров и время работы в минутах (округлить в большую сторону).
    //Для каждой даты из хранилища событий, для которой есть запись о работе повара, должна выводится продолжительность работы в минутах для этой даты.
    //Если повар не работал в какой-то из дней, то с пустыми данными его НЕ выводить (см. 13-Jul-2013)
    //Поваров сортировать по имени
    //
    //Пример:
    //14-Jul-2013
    //Ivanov - 60 min
    //Petrov - 35 min
    //
    //13-Jul-2013
    //Ivanov - 129 min
    //
    //12-Jul-2013
    //Ivanov - 6 min
    //Petrov - 5 min


    public Map<String, Map<String, Integer>> getStatisticForCooks()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Map<EventType, List<EventDataRow>> storageMap = statisticStorage.getStorage();
        List<EventDataRow> list = storageMap.get(EventType.COOKED_ORDER);

        Map<String, Map<String, Integer>> map = new TreeMap<>(Collections.reverseOrder());

        for (EventDataRow event : list)
        {
            CookedOrderEventDataRow cookedOrderEvent = (CookedOrderEventDataRow) event;
            String date = dateFormat.format(cookedOrderEvent.getDate());
            String cookName = cookedOrderEvent.getCookName();
            int cookingTime = cookedOrderEvent.getTime();
            int cookingTimeMin = (cookingTime % 60 == 0) ? (cookingTime / 60) : (cookingTime / 60 + 1);

            if (map.containsKey(date))
            {
                Map<String, Integer> temp = map.get(date);
                if (temp.containsKey(cookName))
                {
                    temp.put(cookName, temp.get(cookName) + cookingTimeMin);
                } else
                {
                    temp.put(cookName, cookingTimeMin);
                }
                map.put(date, temp);
            } else
            {
                Map<String, Integer> temp = new TreeMap<>();
                temp.put(cookName, cookingTimeMin);
                map.put(date, temp);
            }
        }
        return map;
    }

//    public Set<Cook> getCooks() {
//        return cooks;
//    }

    //=========================INNER CLASS:===================================

    //1. Внутри класса StatisticManager создать приватный иннер класс StatisticStorage.
    private class StatisticStorage{
        //3. StatisticStorage будет хранить данные внутри себя в виде мапы/словаря storage.
        //Связь StatisticStorage и Map должна быть has-a
        //Типы для мапы - <EventType, List<EventDataRow>>
        Map<EventType, List<EventDataRow>> storage = new HashMap<>();

        //4. В конструкторе StatisticStorage инициализируй хранилище данными по-умолчанию:
        //например используя цикл, для каждого EventType добавь new ArrayList<EventDataRow>()
        private StatisticStorage(){
            for (EventType type : EventType.values()) {
                storage.put(type, new ArrayList<EventDataRow>());
            }
        }

        //3. Сделай так, чтобы к методу void put(EventDataRow data)
        //нельзя было получить доступ за пределами класса StatisticManager.
        //Воспользуйся особенностями вложенных классов.
        //Чтобы методом put(EventDataRow data) добавить объект data в данные карты,
        // нужен тип события - EventType.
        //В методе  private void put(EventDataRow data) {
        //нужно обратиться к мапе найти лист нужного эвенттипа и в этот лист добавить событие
        private void put(EventDataRow data){
            EventType eventType = data.getType();
            List<EventDataRow> list = null;

            for(Map.Entry<EventType, List<EventDataRow>> pair : storage.entrySet()){
                EventType type = pair.getKey();
                list = pair.getValue();

                if(eventType == type){
                    list.add(data);
                    break;
                }
            }
            storage.put(eventType, list);
        }

        //Дополнительно добавь вспомогательный метод get в класс хранилища, чтобы получить доступ к данным.

        public Map<EventType, List<EventDataRow>> getStorage() {
            return storage;
        }
    }
}
