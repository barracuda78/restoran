package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.Observable;
import java.util.Observer;

public class Waiter implements Observer {


    //в классе Tablet extends Observable:
        //setChanged();
        //notifyObservers(order);

    //     * @param   o     the observable object.
    //     * @param   arg   an argument passed to the <code>notifyObservers</code>
    //     *                 method.
    @Override              // Cook   //Order
    public void update(Observable o, Object arg) {
        ConsoleHelper.writeMessage(arg + " was cooked by " + o);
    }
}
