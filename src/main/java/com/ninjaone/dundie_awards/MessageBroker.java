package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Activity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class MessageBroker {

    private final List<Activity> messages = new LinkedList<>();

    public void sendMessage(final String event) {
        var message = new Activity(LocalDateTime.now(), event);
        messages.add(message);
    }

    public List<Activity> getMessages(){
        return messages;
    }
}
