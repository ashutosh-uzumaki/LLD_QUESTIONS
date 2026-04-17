package design_patterns.creational.singleton.enums_singleton;

enum Logger{
    INSTANCE;

    public void log(String message){
        System.out.println(message);
    }
}
