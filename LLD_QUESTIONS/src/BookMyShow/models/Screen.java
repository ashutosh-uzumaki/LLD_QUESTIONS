package BookMyShow.models;

import BookMyShow.enums.AudioType;
import BookMyShow.enums.ScreenType;

public class Screen {
    private final int screenId;
    private final String name;
    private final ScreenType screenType;
    private final AudioType audioType;

    public Screen(int screenId, String name, ScreenType screenType, AudioType audioType) {
        this.screenId = screenId;
        this.name = name;
        this.screenType = screenType;
        this.audioType = audioType;
    }

    public int getScreenId() {
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
