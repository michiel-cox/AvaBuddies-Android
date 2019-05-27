package com.projectsoa.avabuddies.data.models;

import com.projectsoa.avabuddies.data.models.responses.chat.DialogResponse;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.stfalcon.chatkit.commons.models.IDialog;

import javax.inject.Inject;

public class Dialog implements IDialog<Message> {

    @Inject
    protected LoginRepository loginRepository;

    private String id;
    private User user1;
    private User user2;

    public Dialog(String _id, User user1, User user2) {

        this.id = _id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Dialog(DialogResponse dialog) {
        id = dialog._id;
        user1 = dialog.user1;
        user2 = dialog.user2;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getOtherUser() {
        if(loginRepository.getLoggedInUser().getUser().equals(user1)){
            return  user2;
        }
        else{
            return  user1;
        }
    }
}
