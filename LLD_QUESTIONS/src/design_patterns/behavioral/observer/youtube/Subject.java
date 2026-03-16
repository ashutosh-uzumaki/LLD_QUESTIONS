package design_patterns.behavioral.observer.youtube;

public interface Subject {
    void register(Observer observer);
    void remove(Observer observer);
    void notifyObservers();
}
