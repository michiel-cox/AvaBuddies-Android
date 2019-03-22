package com.projectsoa.avabuddies.screens.main.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.screens.login.LoginActivity;

import androidx.annotation.Nullable;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {


    protected ProfileViewModel viewModel;

    public ProfileFragment() {

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel(ProfileViewModel.class);
    }


    @OnClick(R.id.btn_logout)
    public void logout(){
        Intent intent = new Intent(getBaseActivity(), LoginActivity.class);
        intent.putExtra("logout", true);
        startActivity(intent);
    }

}
