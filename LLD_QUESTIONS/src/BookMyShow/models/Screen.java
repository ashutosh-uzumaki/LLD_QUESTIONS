package BookMyShow.models;

import BookMyShow.enums.AudioType;
import BookMyShow.enums.ScreenType;

public class Screen {
    private final Long screenId;
    private final String name;
    private final ScreenType screenType;
    private final AudioType audioType;
    private final Long theatreId;

    public Screen(Long screenId, String name, ScreenType screenType, AudioType audioType, Long theatreId) {
        this.screenId = screenId;
        this.name = name;
        this.screenType = screenType;
        this.audioType = audioType;
        this.theatreId = theatreId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public String getName() {
        return name;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public AudioType getAudioType() {
        return audioType;
    }
}
