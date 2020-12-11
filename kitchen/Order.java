package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;
import java.io.IOException;
import java.util.List;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        //dishes = ConsoleHelper.getAllDishesForOrder(); //--> закомментил в 18 задаче.
        initDishes();
    }

    //определять, есть ли какие либо блюда в заказе.
    public boolean isEmpty(){
        return dishes.size() == 0;
    }

    //посчитает суммарное время приготовления всех блюд в заказе. в минутах.
    public int getTotalCookingTime(){
        int totalCookingTime = 0;
        for (Dish dish: dishes) {
            totalCookingTime += dish.getDuration();
        }
        return totalCookingTime;
    }


    public List<Dish> getDishes() {
        return dishes;
    }

    //б) в классе Order создай protected метод initDishes(), в котором инициализируй dishes.
    // Вызови этот метод в конструкторе;
    protected void initDishes() throws IOException{
        dishes = ConsoleHelper.getAllDishesForOrder();
    }

    public Tablet getTablet() {
        return tablet;
    }

    @Override
    public String toString(){
        if(dishes.isEmpty()){
            return "";
        }else{
            StringBuilder sb = new StringBuilder();
            for(Dish dish : dishes){
                sb.append(dish.toString());
                sb.append(", ");
            }
            String dishes = sb.toString();
            dishes = dishes.substring(0, dishes.length() - 2);
            return "Your order: [" + dishes + "] of " + tablet;
        }
    }
}
