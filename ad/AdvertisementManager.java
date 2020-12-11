package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.NoAvailableVideoEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.*;


//у каждого планшета будет свой объект менеджера, который будет подбирать оптимальный набор роликов
// и их последовательность для каждого заказа.
//Он также будет взаимодействовать с плеером и отображать ролики.
// обрабатывает рекламное видео.
public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds; //Т.к. продолжительность видео у нас хранится в секундах, то и и время выполнения заказа тоже будем принимать в секундах.

    public AdvertisementManager(int timeSeconds){
        this.timeSeconds = timeSeconds;
    }

    //processVideos() обрабатывает рекламное видео.
    //Метод делает:
    //2.2. Подобрать список видео из доступных, просмотр которых обеспечивает максимальную выгоду.
    //2.3. Если нет рекламных видео, которые можно показать посетителю,
    // то бросить NoVideoAvailableException, которое перехватить в оптимальном месте
    // (подумать, где это место) и с уровнем Level.INFO логировать фразу "No video is available for the order " + order
    //2.4. Отобразить все рекламные ролики, отобранные для показа, в порядке уменьшения стоимости показа одного рекламного ролика в копейках.
    // Вторичная сортировка - по увеличению стоимости показа одной секунды рекламного ролика в тысячных частях копейки.
    //Используйте метод Collections.sort
    //Пример для заказа [Water]:
    //First Video is displaying... 50, 277
    //где First Video - название рекламного ролика
    //где 50 - стоимость показа одного рекламного ролика в копейках
    //где 277 - стоимость показа одной секунды рекламного ролика в тысячных частях копейки (равно 0.277 коп)
    //Используйте методы из класса Advertisement.
    //Также не забудь реализовать п.2.4 из предыдущего задания (вывести на экран все подходящие ролики в порядке уменьшения стоимости показа одного рекламного ролика в копейках.
    // Вторичная сортировка - по увеличению стоимости показа одной секунды рекламного ролика в тысячных частях копейки).
    //Для каждого показанного видео-ролика должен быть вызван метод revalidate().

    //это решение с https://javarush.ru/help/2334
    public void processVideos()
    {
        if (storage.list().isEmpty())
        {
            throw new NoVideoAvailableException();
        }

        List<Advertisement> bestList = chooseBestList(powerList(storage.list()));
        if (bestList.isEmpty())
        {
            throw new NoVideoAvailableException();
        }

        Collections.sort(bestList, new Comparator<Advertisement>(){
            @Override
            public int compare(Advertisement a1, Advertisement a2)
            {
                int result = Long.compare(a2.getAmountPerOneDisplaying(), a1.getAmountPerOneDisplaying());
                if (result != 0) return result;

                long oneSecondCost1 = a1.getAmountPerOneDisplaying() * 1000 / a1.getDuration();
                long oneSecondCost2 = a2.getAmountPerOneDisplaying() * 1000 / a2.getDuration();

                return Long.compare(oneSecondCost1, oneSecondCost2);
            }
        });

        int amount = 0;
        int totalDuration = 0;
        for (Advertisement a : bestList)
        {
            amount += a.getAmountPerOneDisplaying();
            totalDuration += a.getDuration();
        }
        EventDataRow eventDataRow = new VideoSelectedEventDataRow(bestList, amount, totalDuration);
        StatisticManager.getInstance().register(eventDataRow);

        for (Advertisement a : bestList)
        {
            ConsoleHelper.writeMessage(a.getName() + " is displaying... "
                    + a.getAmountPerOneDisplaying() + ", "
                    + a.getAmountPerOneDisplaying() * 1000 / a.getDuration());

            a.revalidate();
        }
    }

    private List<Advertisement> chooseBestList(List<List<Advertisement>> allCombinations)
    {
        Iterator iterator = allCombinations.iterator();
        while (iterator.hasNext())
        {
            List<Advertisement> list = (List<Advertisement>) iterator.next();
            int totalDuration = 0;
            boolean removed = false;
            for (Advertisement ad : list)
            {
                totalDuration += ad.getDuration();
                if (ad.getHits() < 1)
                {
                    removed = true;
                    iterator.remove();
                    break;
                }
            }
            if (!removed)
            {
                if (totalDuration > timeSeconds)
                {
                    iterator.remove();
                }
            }
        }

        Collections.sort(allCombinations, new Comparator<List<Advertisement>>()
        {
            @Override
            public int compare(List<Advertisement> o1, List<Advertisement> o2)
            {
                long sumA1 = 0;
                long sumA2 = 0;
                int durA1 = 0;
                int durA2 = 0;
                for (Advertisement a1 : o1)
                {
                    sumA1 += a1.getAmountPerOneDisplaying();
                    durA1 += a1.getDuration();
                }
                for (Advertisement a2 : o2)
                {
                    sumA2 += a2.getAmountPerOneDisplaying();
                    durA2 += a2.getDuration();
                }
                if (sumA1 != sumA2)
                {
                    return Long.compare(sumA2, sumA1);
                }
                if (durA1 != durA2)
                {
                    return Integer.compare(durA2, durA1);
                }
                return Integer.compare(o1.size(), o2.size());
            }
        });

        return allCombinations.size() != 0 ? allCombinations.get(0) : new ArrayList<Advertisement>();
    }

    private <Advertisement> List<List<Advertisement>> powerList(List<Advertisement> originalList)
    {
        List<List<Advertisement>> lists = new ArrayList<List<Advertisement>>();
        if (originalList.isEmpty())
        {
            lists.add(new ArrayList<Advertisement>());
            return lists;
        }
        List<Advertisement> list = new ArrayList<Advertisement>(originalList);
        Advertisement head = list.get(0);
        List<Advertisement> rest = new ArrayList<Advertisement>(list.subList(1, list.size()));
        for (List<Advertisement> l : powerList(rest))
        {
            List<Advertisement> newList = new ArrayList<Advertisement>();
            newList.add(head);
            newList.addAll(l);
            lists.add(newList);
            lists.add(l);
        }
        return lists;
    }

    //это моё решение:
//    public void processVideos() throws NoVideoAvailableException {
//        if (storage.list().isEmpty()) {
//            // в AdvertisementManager так же регистрируем отсутствие видеоподборки new NoAvailableVideoEventDataRow
//            // перед выбросом исключения new NoVideoAvailableException();
//            //int totalDuration -  время приготовления заказа в секундах
//            StatisticManager.getInstance().register(new NoAvailableVideoEventDataRow(timeSeconds));
//            throw new NoVideoAvailableException();
//        }
//        List<Advertisement> video = new ArrayList<>();
//        for (Object video1 : storage.list()) {
//            Advertisement adv = (Advertisement) video1;
//            video.add(adv);
//        }
//        Collections.sort(video, (Comparator.comparingInt(Advertisement::getDuration)));
//        Collections.sort(video, ((o1, o2) -> (int) (o1.getAmountPerOneDisplaying() - o2.getAmountPerOneDisplaying())));
//        Collections.reverse(video);
//        int freetime = timeSeconds;
//        //Попробуйте вынести код регистрации события перед циклом,
//        // чтобы событие создавалось и регистрировалось один раз,
//        // после чего проигрывалась бы подборка роликов.
//        // При создании события VideoSelectedEventDataRow передайте в конструктор общую сумму за показ всей подборки,
//        // а также общую длительность выбранных роликов.
//
//        //рассчитываю общую сумму за показ всей подборки:
//        long wholeAmount = 0L;
//        //и общую длительность выбранных роликов:
//        int wholeDuration = 0;
//        for (Advertisement vid : video) {
//            if (vid.getDuration() <= freetime && vid.getAmountPerOneDisplaying() > 0) {
//                wholeAmount += vid.getAmountPerOneDisplaying();
//                wholeDuration += vid.getDuration();
//            }
//        }
//
//        StatisticManager.getInstance().register(new VideoSelectedEventDataRow(video, wholeAmount, wholeDuration));
//
//        for (Advertisement vid : video) {
//
//            if (vid.getDuration() <= freetime && vid.getAmountPerOneDisplaying() > 0) {
//                //4. Перед отображением видео должно быть зарегистрировано событие "видео выбрано".
//                //5. Зарегистрируй событие "видео выбрано" перед отображением рекламы пользователю.
//                //конструктор: public VideoSelectedEventDataRow(List<Advertisement> optimalVideoSet, long amount, int totalDuration)
//                //убираю отсюда register по совету с javarush.ru
//                //StatisticManager.getInstance().register(new VideoSelectedEventDataRow(video, vid.getAmountPerOneDisplaying(), vid.getDuration()));
//                ConsoleHelper.writeMessage(vid.getName()+" is displaying... "+vid.getAmountPerOneDisplaying()+", "+vid.getAmountPerOneDisplaying()*1000/vid.getDuration());
//                vid.revalidate();
//                freetime = freetime - vid.getDuration();
//            }
//        }
//
//    }

    //не использую его:
    class MaxCostComparator implements Comparator<Advertisement>{
        @Override
        public int compare(Advertisement a1, Advertisement a2){
            if(a1.getAmountPerOneDisplaying() == a2.getAmountPerOneDisplaying()){
                //тут запилить вторичную сортировку.
                //Вторичная сортировка - по увеличению стоимости показа одной секунды рекламного ролика в тысячных частях копейки. 0,002  копеек/секунду.
                //a1.getAmountPerOneDisplaying() - long - копеек за весь ролик / a1.getDuration() - int - секунд
                if(a1.getAmountPerOneDisplaying()/a1.getDuration() == a2.getAmountPerOneDisplaying()/a2.getDuration()){
                    return 0;
                }else if (a1.getAmountPerOneDisplaying()/a1.getDuration() > a2.getAmountPerOneDisplaying()/a2.getDuration()){
                    return 1;
                }else {
                    return -1;
                }
            }else if(a1.getAmountPerOneDisplaying() > a2.getAmountPerOneDisplaying()){
                return -1;
            } else {
                return 1;
            }
        }
    }
}
















