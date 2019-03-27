package com.projectsoa.avabuddies.utils;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Utils {

    private Context mContext;

    @Inject
    public Utils(Context context) {
        this.mContext = context;
    }
}
