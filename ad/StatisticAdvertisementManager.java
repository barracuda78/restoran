package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//этот класс будет предоставлять информацию из AdvertisementStorage в нужном нам виде.
public class StatisticAdvertisementManager {
    //1. В пакете ad создай StatisticAdvertisementManager,
    // который будет предоставлять информацию из AdvertisementStorage в нужном нам виде.
    //Сделай его синглтоном.

    private static volatile StatisticAdvertisementManager instance;
    private AdvertisementStorage advertisementStorage = AdvertisementStorage.getInstance();


    private StatisticAdvertisementManager(){

    }

    public static StatisticAdvertisementManager getInstance(){
        StatisticAdvertisementManager localInstance = instance;
        if (localInstance == null){
            synchronized (StatisticAdvertisementManager.class){
                localInstance = instance;
                if(localInstance == null){
                    instance = localInstance = new StatisticAdvertisementManager();
                }
            }
        }
        return instance;
    }

    //3. В StatisticAdvertisementManager создай два (или один) метода (придумать самостоятельно),
    // которые из хранилища AdvertisementStorage достанут все необходимые данные -
    // соответственно список активных и неактивных рекламных роликов.
    //Активным роликом считается тот, у которого есть минимум один доступный показ.
    //Неактивным роликом считается тот, у которого количество показов равно 0.

    public List<Advertisement> getActiveAdvertisements(){

        return advertisementStorage.list().stream().filter(x -> x.getHits() > 0).collect(Collectors.toList());
    }

    public List<Advertisement> getNoneActiveAdvertisements(){
        return advertisementStorage.list().stream().filter(x -> x.getHits() == 0).collect(Collectors.toList());
    }
}
