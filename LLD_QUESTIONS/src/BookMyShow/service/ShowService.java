package BookMyShow.service;

import BookMyShow.enums.ShowSeatStatus;
import BookMyShow.models.Show;
import BookMyShow.models.ShowSeat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShowService {
    private final ConcurrentHashMap<Long, List<ShowSeat>> showSeats;
    public ShowService(){
        this.showSeats = new ConcurrentHashMap<>();
    }

    public void addSeat(Long showId, List<ShowSeat> showSeat){
        showSeats.computeIfAbsent(showId, k-> new ArrayList<>()).addAll(showSeat);
    }

    public List<ShowSeat> getSeats(Long showId){
        return showSeats.getOrDefault(showId, new ArrayList<>());
    }

}
