package com.javarush.task.task27.task2712.statistic.event;

import com.javarush.task.task27.task2712.ad.Advertisement;

import java.util.Date;
import java.util.List;

public class VideoSelectedEventDataRow implements EventDataRow {
    private Date currentDate;
    private List<Advertisement> optimalVideoSet;
    private long amount;
    private int totalDuration;
    //optimalVideoSet - список видео-роликов, отобранных для показа
    //amount - сумма денег в копейках
    //totalDuration - общая продолжительность показа отобранных рекламных роликов
    public VideoSelectedEventDataRow(List<Advertisement> optimalVideoSet, long amount, int totalDuration){
        this.optimalVideoSet = optimalVideoSet;
        this.amount = amount;
        this.totalDuration = totalDuration;
        currentDate = new Date();
    }

    @Override
    public EventType getType() {
        return EventType.SELECTED_VIDEOS;
    }

    @Override
    public Date getDate() {
        return currentDate;
    }

    @Override
    public int getTime() {
        return totalDuration;
    }

    public long getAmount() {
        return amount;
    }

    //    @Override
//    public String toString(){
//        return "VideoSelectedEventDataRow : amount = " + amount;
//    }


}
