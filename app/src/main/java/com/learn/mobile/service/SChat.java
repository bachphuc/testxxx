package com.learn.mobile.service;

import android.content.Context;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.ChatMessage;
import com.learn.mobile.model.ChatUser;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
    private long _id;

    {
        _id = System.currentTimeMillis();
    }

    public long getIdentity() {
        return _id++;
    }

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

    List<SocketChatEventListener> userLeftCallbacks = new ArrayList<>();

    public void addUserLeftCallback(SocketChatEventListener callback) {
        processAddCallback(userLeftCallbacks, callback);
    }

    List<SocketChatEventListener> listenUserOnlineCallbacks = new ArrayList<>();

    public void addListenUserOnlineCallback(SocketChatEventListener callback) {
        processAddCallback(listenUserOnlineCallbacks, callback);
    }

    List<SocketChatEventListener> updateMessageCallbacks = new ArrayList<>();

    public void onUpdateMessage(SocketChatEventListener callback) {
        processAddCallback(updateMessageCallbacks, callback);
    }

    public void removeOnUpdateMessageListener(SocketChatEventListener callback) {
        processRemoveCallback(updateMessageCallbacks, callback);
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

    public String createUsername(User user) {
        return "user" + user.getId();
    }

    public void socketLogin() {
        DMobi.log(TAG, "try to login socket...");
        if (viewer != null) {
            username = createUsername(viewer);

            image = viewer.getImages().getNormal().url;
            mine = new ChatUser();
            mine.username = username;
            mine.fullname = viewer.getTitle();
            mine.id = viewer.getId();
            mine.image = image;
            DMobi.log(TAG, "socketLogin: full name: " + viewer.getTitle() + ", username: " + username);

            JSONObject sendData = new JSONObject();
            try {
                sendData.put("username", username);
                sendData.put("image", image);
                sendData.put("user_id", viewer.getId());
                sendData.put("fullname", viewer.getTitle());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("user login", sendData);
        } else {
            DMobi.log(TAG, "user is not login, try login...");
            DUtils.setTimeout(new Runnable() {
                @Override
                public void run() {
                    socketLogin();
                }
            }, 2000);
        }
    }

    public void onLogin(Object... args) {
        isLoginProcessing = false;

        // Display the welcome message
        String message = "Welcome to Socket.IO Chat – ";
        DMobi.log(TAG, message);
        JSONObject data = (JSONObject) args[0];
        try {
            JSONObject userJsonObject = data.getJSONObject("users");
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
                try {
                    String room = (String) args[0];
                    JSONObject user = (JSONObject) args[1];

                    String tmUsername = user.getString("username");
                    DMobi.log(TAG, tmUsername + " request chat with you in room " + room);

                    addUser(tmUsername, user);
                    socket.emit("join room", room);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        processCallback(userLeftCallbacks, data);
    }

    public ChatUser addUser(String username, JSONObject userData) {
        if (DUtils.isEmpty(username)) {
            return null;
        }
        if (username.equals(getUsername())) {
            return null;
        }
        ChatUser user;

        if (users.containsKey(username)) {
            user = users.get(username);
            if (user.is_processing) {
                user.cloneFromJSONObject(userData);
            }
            user.online = true;
        } else {
            user = new ChatUser();
            user.cloneFromJSONObject(userData);
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
            if (tmUsername != null) {
                addUser(tmUsername, data);
            }
            totalUser = data.getInt("numUsers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String username, ChatMessage data) {
        if (DUtils.isEmpty(username) || data == null) {
            DMobi.log(TAG, "addMessage: username or data is null");
            return;
        }
        ChatUser user = this.getUser(username);

        if (data.is_update) {
            DMobi.log(TAG, "addMessage: check is_update message: " + data.getSenderKey());
            ChatMessage message = user.getProcessingMessage(data.getSenderKey());
            if (message != null) {
                DMobi.log(TAG, "addMessage: update message");
                // todo update message and remove processing message
                // todo merge two  data to message
                message.merge(data);
                user.removeProcessingMessage(data.getSenderKey());
                DMobi.log(TAG, "update message");
                if (message.is_processing) {
                    message.is_processing = false;
                }
                processCallback(updateMessageCallbacks, message);
                return;
            }
        }
        if (users.containsKey(username)) {
            user = users.get(username);
        } else {
            JSONObject userData = new JSONObject();
            try {
                userData.put("image", data.senderImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user = addUser(username, userData);
        }
        // todo add user
        if (data.bMine) {
            data.sender = this.getMine();
            data.receiver = user;
        } else {
            data.receiver = this.getMine();
            data.sender = user;
        }
        if (user != null) {
            user.messages.add(data);
        }
        if (data.is_processing) {
            DMobi.log(TAG, "addMessage: check is processing message sender key: " + data.getSenderKey());
            user.addProcessingMessage(data);
        }
        processCallback(talkCallbacks, data);
    }

    public void talk(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
            String tmUsername = (data.has("username") ? data.getString("username") : null);
            String tmImage = (data.has("image") ? data.getString("image") : null);
            if (tmUsername == null) {
                return;
            }

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.cloneFromJSONObject(data);
            chatMessage.senderUsername = tmUsername;
            chatMessage.receiveUsername = getUsername();
            chatMessage.key = getIdentity();
            if (data.has("key")) {
                chatMessage.senderKey = data.getLong("key");
            }
            chatMessage.senderImage = tmImage;
            chatMessage.bMine = false;
            DMobi.log(TAG, "this talk: chatMessage: " + chatMessage.message + ", senderUsername:" + tmUsername);

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
        sendMessage(data, null);
    }

    public ChatMessage sendMessage(ChatMessage message, JSONObject params) {
        if (message == null) {
            return null;
        }
        DMobi.log(TAG, "sendMessage");
        long key = getIdentity();
        message.senderImage = getImage();
        message.senderUsername = getUsername();
        message.bMine = true;
        message.key = key;
        String tmMessage = message.message;
        if (!message.is_processing) {
            message.is_complete = true;
        }
        addMessage(message.receiveUsername, message);
        try {
            JSONObject sendData = new JSONObject();
            sendData.put("username", message.getReceiveUsername());
            sendData.put("message", tmMessage);
            sendData.put("key", key);
            if (params != null) {
                Iterator<String> keys = params.keys();
                while (keys.hasNext()) {
                    String jsonKey = keys.next();
                    Object value = params.get(jsonKey);
                    sendData.put(jsonKey, value);
                }
            }

            socket.emit("talk to user", sendData);

            return message;
        } catch (JSONException e) {
            DMobi.log(TAG, "fail to send message");
            e.printStackTrace();
        }
        return null;
    }

    public void updateMessage(ChatMessage message, JSONObject params) {
        DMobi.log(TAG, "update message");
        if (message == null) {
            return;
        }
        if (message.key == 0) {
            return;
        }
        try {
            JSONObject sendData = new JSONObject();
            sendData.put("username", message.getReceiveUsername());
            sendData.put("message", message.message);
            sendData.put("key", message.key);
            sendData.put("is_complete", message.is_complete);
            if (message.photo != null) {
                sendData.put("photo", message.photo);
            }
            sendData.put("is_update", true);
            if (params != null) {
                Iterator<String> keys = params.keys();
                while (keys.hasNext()) {
                    String jsonKey = keys.next();
                    Object value = params.get(jsonKey);
                    sendData.put(jsonKey, value);
                }
            }
            DMobi.log(TAG, "update message key " + message.getKey() + ", username: " + message.getReceiveUsername());
            socket.emit("talk to user", sendData);
        } catch (JSONException e) {
            DMobi.log(TAG, "fail to send message");
            e.printStackTrace();
        }
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

                addUser(tmUsername, value);
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

    public void uploadPhoto(String path, final DResponse.Complete complete) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("dchat.upload");
        dRequest.addFile(path);
        dRequest.setComplete(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                if (status) {
                    String respondString = (String) o;
                    Gson gson = new GsonBuilder().create();
                    Type type = new TypeToken<SingleObjectResponse<ChatUploadResponseData>>() {
                    }.getType();
                    try {
                        DMobi.log(TAG, "update photo chat success");
                        SingleObjectResponse<ChatUploadResponseData> response = gson.fromJson(respondString, type);
                        if (response.isSuccessfully()) {
                            DMobi.log(TAG, respondString);
                            String[] images = response.data.images;
                            complete.onComplete(true, images);
                        } else {
                            responseError(response, complete);
                        }
                    } catch (JsonParseException e) {
                        DMobi.log("upload photo chat", respondString);
                        networkError(complete);
                    }
                } else {
                    networkError(complete);
                }
            }
        });
        dRequest.upload();
    }

    public static class ChatUploadResponseData {
        String[] images;
    }
}

