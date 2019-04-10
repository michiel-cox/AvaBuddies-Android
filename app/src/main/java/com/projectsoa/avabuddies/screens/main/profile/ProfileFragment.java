package com.projectsoa.avabuddies.screens.main.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.screens.login.LoginActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    protected User user;
    protected ProfileViewModel viewModel;
    @Inject
    protected LoginRepository loginRepository;
    @BindView(R.id.Name)
    protected TextView name;
    @BindView(R.id.Email)
    protected TextView email;
    @BindView(R.id.AboutMe)
    protected TextView aboutMe;

    public ProfileFragment() {
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.user = loginRepository.getLoggedInUser().getUser();

        name.setText(user.getName());
        email.setText(user.getEmail());
        aboutMe.setText("about text moet nog komen");

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
