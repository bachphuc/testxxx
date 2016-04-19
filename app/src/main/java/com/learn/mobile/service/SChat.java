package com.learn.mobile.service;

import android.content.Context;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.model.ChatMessage;
import com.learn.mobile.model.ChatUser;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by BachPhuc on 4/18/2016.
 */
public class SChat extends SBase {
    public static final String TAG = SChat.class.getSimpleName();

    public SChat() {
        itemClass = ChatUser.class;
    }

    @Override
    public List<DMobileModelBase> getData() {
        return super.getData();
    }

    public List<DMobileModelBase> getMessage(String username) {
        if (users.containsKey(username)) {
            ChatUser user = getUser(username);
            return user.getMessages();
        }
        return null;
    }

    public String generalRoomId(String from, String to) {
        if (from.compareTo(to) == 1) {
            return (to + "_" + from);
        }
        return (from + "_" + to);
    }

    Context context;

    public void setContext(Context c) {
        context = c;
    }

    Socket socket = null;
    User viewer;
    ChatUser mine;

    public ChatUser getMine() {
        return mine;
    }

    HashMap<String, ChatUser> users = new HashMap<>();
    int totalUser = 0;
    boolean bSocketReady = false;
    boolean isLoginProcessing = false;
    String username;
    String image;

    public String getImage() {
        return image;
    }

    public User getCurrentViewer() {
        viewer = DMobi.getUser();
        return viewer;
    }

    public static interface SocketChatEventListener {
        void onListener(Object data);
    }

    public static class SimpleSocketResponse {
        String username;
        String message;
        HashMap<String, ChatUser> users;

        public SimpleSocketResponse() {
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void processAddCallback(List<SocketChatEventListener> callbacks, SocketChatEventListener callback) {
        if (callback == null) {
            return;
        }
        if (callbacks.indexOf(callback) == -1) {
            callbacks.add(callback);
        }
    }

    public void processRemoveCallback(List<SocketChatEventListener> callbacks, SocketChatEventListener callback) {
        if (callback == null) {
            return;
        }
        if (callbacks.indexOf(callback) != -1) {
            callbacks.remove(callback);
        }
    }

    public HashMap<String, ChatUser> getUsers() {
        return users;
    }

    List<SocketChatEventListener> newMessageCallBacks = new ArrayList<>();

    public void addNewMessageCallBack(SocketChatEventListener callback) {
        processAddCallback(newMessageCallBacks, callback);
    }

    List<SocketChatEventListener> userJoinCallbacks = new ArrayList<>();

    public void addUserJoinCallbacks(SocketChatEventListener callback) {
        processAddCallback(userJoinCallbacks, callback);
    }

    List<SocketChatEventListener> talkCallbacks = new ArrayList<>();

    public void onNewMessageListener(SocketChatEventListener callback) {
        processAddCallback(talkCallbacks, callback);
    }

    public void removeOnNewMessageListener(SocketChatEventListener callback) {
        processRemoveCallback(talkCallbacks, callback);
    }

    List<SocketChatEventListener> userleftCallbacks = new ArrayList<>();

    public void addUserLeftCallback(SocketChatEventListener callback) {
        processAddCallback(userleftCallbacks, callback);
    }

    List<SocketChatEventListener> listenUserOnlineCallbacks = new ArrayList<>();

    public void addListenUserOnlineCallback(SocketChatEventListener callback) {
        processAddCallback(listenUserOnlineCallbacks, callback);
    }

    List<SocketChatEventListener> readyCallbacks = new ArrayList<>();

    public void onReady(SocketChatEventListener callback) {
        processAddCallback(readyCallbacks, callback);
    }

    public void removeOnReadyListener(SocketChatEventListener callback) {
        processRemoveCallback(readyCallbacks, callback);
    }

    public void ready(Object data) {
        processCallback(readyCallbacks, data);
    }

    public void emit(String event, String message) {
        if (socket == null) {
            return;
        }
        socket.emit(event, message);
    }

    public void socketLogin() {
        DMobi.log(TAG, "try to login socket...");
        if (viewer != null) {
            username = viewer.getTitle();
            image = viewer.getImages().getNormal().url;
            mine = new ChatUser();
            mine.username = username;
            mine.image = image;
            DMobi.log(TAG, "add user " + viewer.getTitle());
            socket.emit("add user", viewer.getTitle(), image);
        } else {
            DMobi.log(TAG, "user is not login, please login to continue...");
            // socketLogin();
        }
    }

    public void onLogin(Object... args) {
        isLoginProcessing = false;

        // Display the welcome message
        String message = "Welcome to Socket.IO Chat – ";
        DMobi.log(TAG, message);
        JSONObject data = (JSONObject) args[0];
        try {
            JSONObject userJsonObject = (JSONObject) data.getJSONObject("users");
            listenUserOnline(userJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bSocketReady = true;
        ready(data);
    }

    public void listenEvents() {
        DMobi.log(TAG, "listenEvents");
        socket.on("login", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                DMobi.log(TAG, "socket chat login success");
                onLogin(args);
            }
        });

        // Whenever the server emits "new message", update the chat body
        socket.on("new message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                DMobi.log(TAG, "new message");
                addNewMessage(args);
            }
        });

        // Whenever the server emits "user joined", log it in the chat body
        socket.on("user joined", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                userJoin(args);
            }
        });

        // Whenever the server emits "user left", log it in the chat body
        socket.on("user left", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                userLeft(args);
            }
        });

        // Whenever the server emits "typing", show the typing message
        socket.on("typing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                typing(data);
            }
        });

        // Whenever the server emits "stop typing", kill the typing message
        socket.on("stop typing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                stopTyping(data);
            }
        });
        socket.on("talk", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                DMobi.log(TAG, "talk");
                talk(args);
            }
        });

        // listen request room
        socket.on("request room", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length < 2) {
                    return;
                }
                String room = (String) args[0];
                String tmUsername = (String) args[1];
                String tmImage = (String) args[2];
                DMobi.log(TAG, tmUsername + " request chat with you in room " + room);
                addUser(tmUsername, tmImage);
                socket.emit("join room", room);

            }
        });
        socket.on("get user online", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                DMobi.log(TAG, "get user online success");
                listenUserOnline(args);
            }
        });
    }

    public void init() {
        if (!bSocketReady) {
            if (isLoginProcessing) {
                return;
            }
            viewer = getCurrentViewer();
            isLoginProcessing = true;
            if (socket == null) {
                try {
                    socket = IO.socket(DConfig.SOCKET_URL);
                    socket.connect();
                    listenEvents();
                    socketLogin();

                } catch (URISyntaxException e) {
                    DMobi.log(TAG, e.getMessage());
                }
            } else {
                socketLogin();
            }

        } else {
            DMobi.log(TAG, "ready");
            SimpleSocketResponse response = new SimpleSocketResponse();
            response.users = users;
            ready(response);
        }
    }

    public String getUsername() {
        return (username);
    }

    public void typing(Object data) {

    }

    public void stopTyping(Object data) {

    }

    public void processCallback(List<SocketChatEventListener> callbacks, Object data) {
        if (callbacks != null && callbacks.size() > 0) {
            for (int i = 0; i < callbacks.size(); i++) {
                SocketChatEventListener callback = callbacks.get(i);
                callback.onListener(data);
            }
        }
    }

    public void addNewMessage(Object data) {
        processCallback(newMessageCallBacks, data);
    }

    public void userLeft(Object... args) {
        DMobi.log(TAG, "addUserLeftCallback");
        JSONObject data = (JSONObject) args[0];
        try {
            String tmUsername = data.getString("username");
            if (tmUsername != null) {
                if (users.containsKey(tmUsername)) {
                    ChatUser user = users.get(tmUsername);
                    if (user != null) {
                        user.online = false;
                    }
                }
            }
            totalUser = data.getInt("numUsers");
        } catch (JSONException e) {
            DMobi.log(TAG, e.getMessage());
            return;
        }
        processCallback(userleftCallbacks, data);
    }

    public ChatUser addUser(String username, String image) {
        if (DUtils.isEmpty(username)) {
            return null;
        }
        if (username.equals(getUsername())) {
            return null;
        }
        ChatUser user;

        if (users.containsKey(username)) {
            user = users.get(username);
            user.online = true;
        } else {
            user = new ChatUser();
            user.username = username;
            user.online = true;
            if (image != null) {
                user.image = image;
            }

            users.put(username, user);
            data.add(user);
        }
        SimpleSocketResponse response = new SimpleSocketResponse();
        response.username = username;
        processCallback(userJoinCallbacks, response);

        return user;
    }

    public void userJoin(Object... args) {
        DMobi.log(TAG, "userJoin");
        JSONObject data = (JSONObject) args[0];

        try {
            String tmUsername = data.getString("username");
            String tmImage = data.getString("image");
            if (tmUsername != null) {
                addUser(tmUsername, tmImage);
            }
            totalUser = data.getInt("numUsers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String username, ChatMessage data) {
        if (users.containsKey(username)) {
            users.get(username).messages.add(data);
        } else {
            ChatUser user = addUser(username, data.image);
            if (user != null) {
                user.messages.add(data);
            }
        }
        processCallback(talkCallbacks, data);
    }

    public void talk(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
            String tmUsername = data.getString("username");
            String tmMessage = data.getString("message");
            String tmImage = data.getString("image");
            if (tmUsername == null || tmMessage == null) {
                return;
            }

            DMobi.log(TAG, "this talk: chatMessage: " + tmMessage + ", username:" + tmUsername);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.username = tmUsername;
            chatMessage.message = tmMessage;
            chatMessage.image = tmImage;
            chatMessage.bMine = false;
            addMessage(tmUsername, chatMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startChat(String username) {
        socket.emit("request room", username);
    }

    public ChatUser getUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        } else {
            return null;
        }
    }

    public void sendMessage(ChatMessage data) {
        if (data == null) {
            return;
        }
        DMobi.log(TAG, "sendMessage");
        ChatMessage message = new ChatMessage();
        message.username = getUsername();
        message.message = data.message;
        message.image = getImage();
        message.bMine = true;
        addMessage(data.username, message);
        socket.emit("talk to user", data.username, data.message);
    }

    public void listenUserOnline(Object... args) {
        DMobi.log(TAG, "listenUserOnline");
        JSONObject data = (JSONObject) args[0];
        if (data == null) {
            return;
        }

        Iterator<String> iter = data.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                JSONObject value = (JSONObject) data.get(key);
                String tmUsername = value.getString("username");
                Double tmOnline = value.getDouble("online_time");
                String tmImage = value.getString("image");
                ChatUser newUser = addUser(tmUsername, tmImage);
                if (newUser != null) {
                    newUser.online_time = tmOnline;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        processCallback(listenUserOnlineCallbacks, users);
    }

    public void getUserOnlineFromServer() {
        DMobi.log(TAG, "getUserOnlineFromServer");
        socket.emit("get user online");
    }
}

