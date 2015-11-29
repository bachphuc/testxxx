package com.learn.mobile.library.dmobi.event;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.model.DMobileModelBase;

import java.util.ArrayList;

/**
 * Created by 09520_000 on 9/1/2015.
 */
public class Event {
    // Define some event in app
    public static final String EVENT_UPDATE_PROFILE = "EVENT_UPDATE_PROFILE";
    public static final String EVENT_LOADMORE_FEED = "EVENT_LOADMORE_FEED";
    public static final String EVENT_REFRESH_FEED = "EVENT_REFRESH_FEED";
    public static final String EVENT_LIST_BASE_FRAGMENT_LOADED = "EVENT_LIST_BASE_FRAGMENT_LOADED";
    public static final String EVENT_LIST_BASE_FRAGMENT_LOAD_COMPLETED = "EVENT_LIST_BASE_FRAGMENT_LOAD_COMPLETED";
    public static final String EVENT_LOCK_REFRESH_RECYCLER_VIEW = "EVENT_LOCK_REFRESH_RECYCLER_VIEW";
    public static final String EVENT_LOGIN_SUCCESS = "EVENT_LOGIN_SUCCESS";
    public static final String EVENT_LOGOUT_SUCCESS = "EVENT_LOGOUT_SUCCESS";
    public static final String EVENT_OBJECT_UPDATE = "EVENT_OBJECT_UPDATE";
    public static final String EVENT_FEED_UPDATE_VIEW = "EVENT_FEED_UPDATE_VIEW";

    private ArrayList<Action> actions = new ArrayList<Action>();
    private String eventType;

    public Event() {

    }

    public Event(Action _action) {
        actions.add(_action);
    }

    public Event(Action _action, String _actionType) {
        actions.add(_action);
        eventType = _actionType;
    }

    public void addAction(Action _action) {
        actions.add(_action);
    }

    public void setEventType(String _actionType) {
        eventType = _actionType;
    }

    public String getEventType() {
        return eventType;
    }

    public void fireAction(Object o) {
        if (actions != null) {
            Action action;
            for (int i = 0; i < actions.size(); i++) {
                action = actions.get(i);
                action.fireAction(eventType, o);
            }
        }
    }

    public void clearAction() {
        actions.clear();
    }

    public interface Action {
        void fireAction(String eventType, Object o);
    }

    public static class ModelAction {
        public ModelAction(String type, int id, DMobileModelBase base) {
            this.modelType = type;
            this.modelId = id;
            this.modelBase = base;
        }

        public String modelType;
        public int modelId;
        public DMobileModelBase modelBase;
    }

    public static ModelAction getModelActionInstance(String type, int id, DMobileModelBase base) {
        ModelAction modelAction = new ModelAction(type, id, base);
        return modelAction;
    }
}
