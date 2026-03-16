package design_patterns.creational.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {
    private Map<String, String> configMap = new ConcurrentHashMap<>();
    private static ConfigManager instance;
    private ConfigManager(){

    }

    public static ConfigManager getInstance(){
        if(instance == null){
            synchronized (ConfigManager.class){
                if(instance == null){
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public void setConfig(String key, String value) {
        configMap.put(key, value);
    }

    public String getConfig(String key){
        return configMap.get(key);
    }
}
