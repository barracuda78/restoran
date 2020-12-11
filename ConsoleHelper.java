package com.javarush.task.task27.task2712;


import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class ConsoleHelper {

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString() throws IOException {
        String s = null;
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        s = bufferedReader.readLine();
        return s;
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> list = new ArrayList<>();
        writeMessage(Dish.allDishesToString());
        writeMessage("Введите блюдо: ");
        String enteredString;

        //выполнять цикл заказа блюд и добавления их в list пока не будет введено exit с консоли.
        while(true){
            //прочитать введенную строку и записать ее в enteredString;
            enteredString = readString();
            if (enteredString.equals("exit")) {
                break;
            }
            //создать вассив блюд из всех значений ENUM Dishes.
            Dish[] dishes = Dish.values();
            //создать булеан переменную, чтобы знать, совпаол введенное значение с блюдом из меню? Если да - меняем  на true - т.е. будем добавлять блюдо в list;
            boolean willBeAdded = false;
            //создать массив строк со значениями - строковаыми эквивалентами ENUM
            //String[] dishesNames = new String[dishes.length];

            //заполнить массив строк строковаыми эквивалентами ENUM для сравнения с введенной строкой-блюдом.
            for (int i = 0; i < dishes.length; i++) {
                //dishesNames[i] = dishes[i].toString();
                //если введенная строка совпадает со строковым представлением блюда,
                if(enteredString.equals(dishes[i].toString())){
                    //добавляем блюдо в List
                    list.add(dishes[i]);
                    willBeAdded = true;     //выставляем флаг willBeAdded.
                }
            }

            //если флаг willBeAdded все еще false - значит пишем, что такого блюда нет.
            if (!willBeAdded) {
                 writeMessage("такого блюда нет");
            }

        }

        return list;
    }
}
