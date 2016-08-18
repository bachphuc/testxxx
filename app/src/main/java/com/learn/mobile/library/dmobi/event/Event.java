package com.learn.mobile.library.dmobi.event;

import com.learn.mobile.model.DMobileModelBase;

import java.util.ArrayList;

/**
 * Created by 09520_000 on 9/1/2015.
 */
public class Event {

    private ArrayList<Action> actions = new ArrayList<>();
    private String eventType;

    public Event() {

    }

    public Event(Action _action) {
        actions.add(_action);
    }

    public Event(Action _action, String _actionType) {
        if (actions.indexOf(_action) != -1) {
            return;
        }
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
