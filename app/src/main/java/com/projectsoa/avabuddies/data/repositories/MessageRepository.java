package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.Message;
import com.projectsoa.avabuddies.data.models.responses.chat.MessageResponse;
import com.projectsoa.avabuddies.data.services.MessageService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class MessageRepository {
    private MessageService messageService;

    public MessageRepository(MessageService messageService){ this.messageService = messageService; }

    public Single<List<Message>> getList(){
        return messageService.fetchList().map(MessageListResponse -> {
            List<Message> messages = new ArrayList<>();
            for(MessageResponse message : MessageListResponse.messages){
                messages.add(new Message(message));
            }
            return messages;
        });
    }
}
