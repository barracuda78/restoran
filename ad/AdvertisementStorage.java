package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

//хранилище рекламных роликов.
public class AdvertisementStorage {

    private static volatile AdvertisementStorage instance;
    private final List<Advertisement> videos = new ArrayList<>();

    private AdvertisementStorage(){
        Object someContent = new Object();
        //Constructor для Advertisement: Object content, String name, long initialAmount, int hits, int duration
        add(new Advertisement(someContent, "First Video", 5000, 100, 3 * 60)); // 3 min
        add(new Advertisement(someContent, "Second Video", 100, 10, 15 * 60)); //15 min
        add(new Advertisement(someContent, "Third Video", 400, 2, 10 * 60)); //10 min
//        add(new Advertisement(someContent, "четвертое видео", 400, 4, 10 * 60)); //10 min
//        add(new Advertisement(someContent, "Additional video", 400, 3, 10 * 60)); //10 min

//        add(new Advertisement(someContent, "First Video", 5000, 100, 3 * 60)); // 3 min
//        add(new Advertisement(someContent, "Second Video", 100, 10, 15 * 60)); //15 min
//        add(new Advertisement(someContent, "Third Video", 400, 1, 10 * 60)); //10 min
//        add(new Advertisement(someContent, "четвертое видео", 400, 1, 10 * 60)); //10 min
//        add(new Advertisement(someContent, "Additional video", 400, 1, 10 * 60)); //10 min

    }

    public static AdvertisementStorage getInstance() {
        AdvertisementStorage localInstance = instance;
        if (localInstance == null){
            synchronized ( AdvertisementStorage.class){
                localInstance = instance;
                if(localInstance == null){
                    instance = localInstance = new AdvertisementStorage();
                }
            }
        }
        return localInstance;
    }

    //вернет список всех существующих доступных видео.
    public List<Advertisement> list(){
        return videos;
    }

    //добавит новое видео в список videos.
    public void add(Advertisement advertisement){
        videos.add(advertisement);
    }

}
