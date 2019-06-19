package com.projectsoa.avabuddies.data.models;

import com.projectsoa.avabuddies.data.models.responses.chat.DialogResponse;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IUser;

import javax.inject.Inject;

public class Dialog implements IDialog<Message> {

    private String id;
    private User user1;
    private User user2;
    private LoginRepository loginRepository;

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
        LoginRepository login = (LoginRepository) loginRepository;
        if(login.getLoggedInUser().getUser().equals(user1)){
            return  user2;
        }
        else{
            return  user1;
        }
    }

    public void setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
}
