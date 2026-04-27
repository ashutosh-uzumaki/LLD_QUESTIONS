package BookMyShow.models;

public class Movie {
     private final Long movieId;
     private final String name;
     private final int durationInMinutes;
     private final String genre;

     public Movie(Long movieId, String name, int durationInMinutes, String genre){
         this.movieId = movieId;
         this.name = name;
         this.durationInMinutes = durationInMinutes;
         this.genre = genre;
     }

    public Long getMovieId() {
        return movieId;
    }

    public String getName() {
        return name;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getGenre() {
        return genre;
    }
}
