package com.projectsoa.avabuddies.utils;

import android.content.Context;
import android.widget.Toast;

import com.projectsoa.avabuddies.App;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Utils {

    private Context mContext;

    @Inject
    public Utils(Context context) {
        this.mContext = context;
    }

    public void showToastError(String message){
        if(message == null || message.isEmpty()) return;
        Toast toast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
    public void showToastMessage(String message){
        if(message == null || message.isEmpty()) return;
        Toast toast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
