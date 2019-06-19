package com.projectsoa.avabuddies.screens.main.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.LocalStorage.AppDatabase;
import com.projectsoa.avabuddies.data.LocalStorage.ChatMessageModel;
import com.projectsoa.avabuddies.data.models.Dialog;
import com.projectsoa.avabuddies.data.models.Message;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.chat.MessageResponse;
import com.projectsoa.avabuddies.data.repositories.ChatMessageRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.MessageRepository;
import com.projectsoa.avabuddies.data.sockets.SocketIO;
import com.projectsoa.avabuddies.utils.Utils;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;

public class ChatFragment extends BaseFragment implements MessageInput.InputListener,
        MessageInput.TypingListener, MessagesListAdapter.OnLoadMoreListener {

    @Inject
    protected ChatMessageRepository chatMessageRepository;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected MessageRepository messageRepository;
    @Inject
    protected SocketIO socketIO;

    @BindView(R.id.messagesList)
    protected MessagesList messagesList;
    @BindView(R.id.input)
    protected MessageInput input;

    private Dialog chat;

    protected List<Message> messageList;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    protected Utils utils;

    public static ChatFragment newInstance(Dialog dialog) {
        ChatFragment fragment = new ChatFragment();
        fragment.setChat(dialog);
        return fragment;
    }

    public void setChat(Dialog dialog) {
        chat = dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketIO.getmSocket().on(chat.getId(), onNewMessage);
        socketIO.connect();
        socketIO.setUserOnline(loginRepository.getLoggedInUser().getUser().getId());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        utils = new Utils(getContext());
        chat.setLoginRepository(loginRepository);
        User user = (User) chat.getOtherUser();
        messagesAdapter = new MessagesListAdapter<>(user.getId(), imageLoader);
        messagesAdapter.setLoadMoreListener(this);

        loadMessages();

        MessageInput input = view.findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_chat;
    }

    private void initAdapter() {
        messagesAdapter.addToEnd(messageList, false);
        this.messagesList.setAdapter(messagesAdapter);
    }

    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        //loadMessages();
    }

    protected void loadMessages() {
        chatMessageRepository.getTask(chat.getId()).observe(this, chatMessageModels -> {
            messageList = new ArrayList<>();
            for (ChatMessageModel messageModel : chatMessageModels
            ) {
                messageList.add(new Message(messageModel.id, messageModel.userId, messageModel.text, new Date(messageModel.createdAt)));
                initAdapter();
            }
        });
    }

    public boolean onSubmit(CharSequence input) {
        Gson gson = new Gson();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date date = new Date();
        String randomId = Long.toString(UUID.randomUUID().getLeastSignificantBits());
        Message message = new Message(randomId, loginRepository.getLoggedInUser().getUser().getId(), input.toString(), date);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.id = message.getId();
        messageResponse.chatId = chat.getId();
        messageResponse.senderId = loginRepository.getLoggedInUser().getUser().getId();
        messageResponse.message = message.getText();
        messageResponse.dateTime = date;
        socketIO.sendMessage(chat.getId(), gson.toJson(messageResponse));

        messagesAdapter.addToStart(message, true);
        this.messagesList.setAdapter(messagesAdapter);

        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.id = randomId;
        chatMessageModel.userId = loginRepository.getLoggedInUser().getUser().getId();
        chatMessageModel.chatId = chat.getId();
        chatMessageModel.createdAt = date.toString();
        chatMessageModel.text = message.getText();

        chatMessageRepository.insertTask(chatMessageModel);

        return true;
    }

    public void onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status));
    }

    public void onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status));
    }

    private Emitter.Listener onNewMessage = args -> {
        getActivity().runOnUiThread(() -> {
            messageList = new ArrayList<>();

            Gson gson = new Gson();
            MessageResponse response = gson.fromJson(String.valueOf(args[0]), MessageResponse.class);
            messageList.add(new Message(response));

            String randomId = Long.toString(UUID.randomUUID().getLeastSignificantBits());
            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.id = randomId;
            chatMessageModel.userId = response.senderId;
            chatMessageModel.chatId = chat.getId();
            chatMessageModel.createdAt = response.dateTime.toString();
            chatMessageModel.text = response.message;

            chatMessageRepository.insertTask(chatMessageModel);

            initAdapter();
            // add the message to view
            //addMessage(username, message);
        });
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        socketIO.getmSocket().disconnect();
        socketIO.getmSocket().off(chat.getId(), this.onNewMessage);
    }
}
