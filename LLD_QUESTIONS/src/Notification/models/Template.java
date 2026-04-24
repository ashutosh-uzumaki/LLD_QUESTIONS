package Notification.models;

import Notification.enums.NotificationType;

import java.util.HashMap;
import java.util.Map;

public class Template {
    private Map<NotificationType, String> templates;

    public Template(){
        templates = new HashMap<>();
    }

    public boolean setTemplate(NotificationType notificationType, String template){
        templates.put(notificationType, template);
        return true;
    }

    public String getTemplate(NotificationType notificationType){
        return templates.get(notificationType);
    }

    public String render(NotificationType notificationType, Map<String, String> placeholders){
        String template = templates.get(notificationType);
        if(template == null){
            return null;
        }
        for(Map.Entry<String, String> entry : placeholders.entrySet()){
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }

}
