package com.javarush.task.task27.task2712.kitchen;

public enum Dish {

    //1. Предположим, что нам известно время приготовления каждого блюда в минутах.
    // Захардкодим его в классе Dish.
    //1.1. Измени создание элементов enum - Fish(25), Steak(30), Soup(15), Juice(5), Water(3);

    Fish(25),
    Steak(30),
    Soup(15),
    Juice(5),
    Water(3);

    private int duration;

    Dish(int duration){
        this.duration = duration;
    }

    //Пример: "Fish, Steak, Soup, Juice, Water, ". Формируй строку динамически.
    public static String allDishesToString(){
        Dish[] dishes = Dish.values();
        StringBuilder sb = new StringBuilder();
        for (Dish d: dishes) {
            sb.append(d.toString());
            sb.append(", ");
        }
        String s = sb.toString();
        s = s.substring(0, s.length()-2);
       return s;
    }

    public int getDuration() {
        return duration;
    }
}
