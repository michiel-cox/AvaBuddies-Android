package com.projectsoa.avabuddies.screens.main.chat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Message;
import com.projectsoa.avabuddies.screens.main.chat.Temp.MessagesFixtures;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Date;

public class ChatFragment extends BaseFragment implements MessagesListAdapter.OnLoadMoreListener{

    private static final int TOTAL_MESSAGES_COUNT = 100;

    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private Date lastLoadedDate;
    private MessagesList messagesList;
    private MessageInput input;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.input = view.findViewById(R.id.input);
        this.messagesList = view.findViewById(R.id.messagesList);
        initAdapter();
        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true);
        // input.setInputListener(this);
        // input.setTypingListener(this);
        // input.setAttachmentsListener(this);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_chat;
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesAdapter.setLoadMoreListener(this);
        this.messagesList.setAdapter(messagesAdapter);
    }

    public void onAddAttachments() {

    }

    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
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
}
