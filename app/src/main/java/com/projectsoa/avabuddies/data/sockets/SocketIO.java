package com.projectsoa.avabuddies.data.sockets;

import android.text.TextUtils;

import com.github.nkzawa.emitter.Emitter;
import com.projectsoa.avabuddies.Constants;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketIO {
    private Socket mSocket;

    public SocketIO() {
        try {
            mSocket = IO.socket(Constants.API_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void connect() {
        mSocket.connect();
    }

    public void setUserOnline(String id) {
        mSocket.emit("user online", id);
    }

    public void sendMessage(String event, String message) {//chat id, Message json
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mSocket.emit(message);
    }

    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                  /*  String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(username, message);*/
                }
            };
        }
    };
}
