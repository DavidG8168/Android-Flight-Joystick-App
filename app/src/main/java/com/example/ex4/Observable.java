package com.example.ex4;
// The observable.
public interface Observable {
    // Adds an observer.
    void addToObserver(Observer observer);
    // Notifies the observer.
    void notifyObservers(Information information);
}
