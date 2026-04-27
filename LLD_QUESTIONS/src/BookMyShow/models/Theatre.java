package BookMyShow.models;

public class Theatre {
    private final Long theatreId;
    private final String name;
    private final String city;
    private final String address;

    public Theatre(Long theatreId, String name, String city, String address) {
        this.theatreId = theatreId;
        this.name = name;
        this.city = city;
        this.address = address;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }
}
