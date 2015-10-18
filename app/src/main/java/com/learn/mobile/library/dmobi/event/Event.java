package com.learn.mobile.library.dmobi.event;

import java.util.ArrayList;

/**
 * Created by 09520_000 on 9/1/2015.
 */
public class Event {
    // define some event in app
    public static final String EVENT_UPDATE_PROFILE = "EVENT_UPDATE_PROFILE";
    public static final String EVENT_LOADMORE_FEED = "EVENT_LOADMORE_FEED";
    public static final String EVENT_REFRESH_FEED = "EVENT_REFRESH_FEED";
    public static final String EVENT_LIST_BASE_FRAGMENT_LOADED = "EVENT_LIST_BASE_FRAGMENT_LOADED";
    public static final String EVENT_LIST_BASE_FRAGMENT_LOAD_COMPLETED = "EVENT_LIST_BASE_FRAGMENT_LOAD_COMPLETED";
    public static final String EVENT_LOCK_REFRESH_RECYCLER_VIEW = "EVENT_LOCK_REFRESH_RECYCLER_VIEW";

    private ArrayList<Action> actions = new ArrayList<Action>();
    private String eventType;
    public Event(){

    }

    public Event(Action _action){
        actions.add(_action);
    }

    public Event(Action _action, String _actionType){
        actions.add(_action);
        eventType = _actionType;
    }

    public void addAction(Action _action){
        actions.add(_action);
    }

    public void setEventType(String _actionType){
        eventType = _actionType;
    }

    public String getEventType(){
        return eventType;
    }

    public void fireAction(Object o){
        if(actions != null){
            Action action;
            for(int i = 0; i < actions.size(); i++){
                action = actions.get(i);
                action.fireAction(eventType, o);
            }
        }
    }

    public void clearAction(){
        actions.clear();
    }

    public interface Action {
        void fireAction(String eventType, Object o);
    }
}
