package com.projectsoa.avabuddies.screens.main.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.nkzawa.socketio.client.Socket;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Dialog;
import com.projectsoa.avabuddies.data.models.Message;
import com.projectsoa.avabuddies.data.repositories.MessageRepository;
import com.projectsoa.avabuddies.data.sockets.SocketIO;
import com.projectsoa.avabuddies.screens.main.chat.Temp.MessagesFixtures;
import com.projectsoa.avabuddies.utils.Utils;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChatFragment extends BaseFragment implements MessagesListAdapter.OnLoadMoreListener{

    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    protected Utils utils;

    @Inject
    protected MessageRepository messageRepository;
    @Inject
    protected SocketIO socketIO;

    private Date lastLoadedDate;
    @BindView(R.id.messagesList)
    protected MessagesList messagesList;
    @BindView(R.id.input)
    protected MessageInput input;
    protected List<Message> messageList;
    private Dialog chat;
    private Socket mSocket;

    public static ChatFragment newInstance(Dialog dialog) {
        ChatFragment fragment = new ChatFragment();
        fragment.setChat(dialog);
        return fragment;
    }

    public void setChat(Dialog dialog){
        chat = dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket = socketIO.getmSocket();
        socketIO.connect();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        utils = new Utils(getContext());
        messagesAdapter = new MessagesListAdapter<>(chat.getOtherUser().getId(), imageLoader);
        messagesAdapter.setLoadMoreListener(this);

        loadMessages();

        // input.setInputListener(this);
        // input.setTypingListener(this);
        // input.setAttachmentsListener(this);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_chat;
    }

    private void initAdapter() {


        lastLoadedDate = messageList.get(messageList.size() - 1).getCreatedAt();
        messagesAdapter.addToEnd(messageList, false);

        this.messagesList.setAdapter(messagesAdapter);
    }

    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
            loadMessages();
    }

    protected void loadMessages() {

        messageRepository.getList().subscribe(messages -> {
            runOnUiThread(() -> {
                this.messageList = messages;
                initAdapter();
            });

        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.error_chats))));

    }

    public boolean onSubmit(CharSequence input) {
        messagesAdapter.addToStart(
                MessagesFixtures.getTextMessage(input.toString()), true);
        return true;
    }

    public void onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status));
    }

    public void onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(chat.getId(), socketIO.onNewMessage);
    }
}
