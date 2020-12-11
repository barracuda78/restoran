package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;

//а) создай класс TestOrder - наследник Order - в пакете родителя;
public class TestOrder extends Order{

    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    //г) переопредели initDishes в классе-наследнике TestOrder:
    // проинициализируй поле dishes пустым списком и заполни его случайным набором блюд;
    @Override
    protected void initDishes() throws IOException {
        int size = (int)((Math.random()) * 4) + 1;
        dishes = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            int dishNumber = (int)((Math.random() * 5) + 1);

            switch(dishNumber){
                case 1 :
                    dishes.add(Dish.Fish);
                    break;
                case 2 :
                    dishes.add(Dish.Steak);
                    break;
                case 3 :
                    dishes.add(Dish.Soup);
                    break;
                case 4 :
                    dishes.add(Dish.Juice);
                    break;
                case 5 :
                    dishes.add(Dish.Water);
                    break;
                default:
                    dishes.add(Dish.Fish);
            }
        }

    }
}
