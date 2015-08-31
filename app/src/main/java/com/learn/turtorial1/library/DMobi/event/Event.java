package com.learn.turtorial1.library.dmobi.event;

/**
 * Created by 09520_000 on 9/1/2015.
 */
public class Event {
    // define some event in app
    public static final String EVENT_UPDATE_PROFILE = "EVENT_UPDATE_PROFILE";

    private Action action;
    public Event(){

    }

    public Event(Action _action){
        action = _action;
    }

    public void setAction(Action _action){
        action = _action;
    }

    public void fireAction(Object o){
        if(action != null){
            action.fireAction(o);
        }
    }

    public interface Action {
        void fireAction(Object o);
    }
}
