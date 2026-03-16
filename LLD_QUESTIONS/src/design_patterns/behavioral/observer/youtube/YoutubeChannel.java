package design_patterns.behavioral.observer.youtube;

import java.util.ArrayList;
import java.util.List;

public class YoutubeChannel implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private final String name;
    public YoutubeChannel(String name){
        this.name = name;
    }

    @Override
    public void register(Observer observer){
        observers.add(observer);
    }

    @Override
    public void remove(Observer observer){
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(){
        for(Observer observer: observers){
            observer.update("New video uploaded");
        }
    }

    public void uploadVideo(String title){
        System.out.println("New video uploaded with title: "+title);
        notifyObservers();
    }
}
