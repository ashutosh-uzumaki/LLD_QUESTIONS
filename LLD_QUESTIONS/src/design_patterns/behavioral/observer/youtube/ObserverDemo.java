package design_patterns.behavioral.observer.youtube;

public class ObserverDemo {
    public static void main(String[] args) {
        YoutubeChannel anime = new YoutubeChannel("Naruto Uzumaki");
        Subscriber ashutosh = new Subscriber("Ashu");
        Subscriber ankita = new Subscriber("Anki");

        anime.register(ashutosh);
        anime.register(ankita);

        anime.uploadVideo("EP 1");
    }
}
