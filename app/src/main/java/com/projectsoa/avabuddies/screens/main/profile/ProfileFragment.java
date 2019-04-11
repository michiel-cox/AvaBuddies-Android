package com.projectsoa.avabuddies.screens.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.login.LoginActivity;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.utils.Utils;

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
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected Utils utils;

    @BindView(R.id.name)
    protected TextView name;
    @BindView(R.id.email)
    protected TextView email;
    @BindView(R.id.info)
    protected TextView info;
    @BindView(R.id.full_name)
    protected TextView fullName;
    @BindView(R.id.profile)
    protected ImageView profile;
    @BindView(R.id.location)
    protected Switch location;



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




        if(!user.getImage().isEmpty()) {
            try {
                byte[] imageByteArray = Base64.decode(user.getImage(), Base64.DEFAULT);
                Glide.with(this)
                        .asBitmap()
                        .apply(RequestOptions.circleCropTransform())
                        .load(imageByteArray)
                        .into(profile);
            } catch (IllegalArgumentException ignored) {

            }
        }

        // Set values of user account
        location.setChecked(user.isSharelocation());
        location.setClickable(false);
        name.setText(user.getName());
        fullName.setText(user.getName());
        email.setText(user.getEmail());
        info.setText(user.getAboutme());

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


    @OnClick(R.id.updateThisUser)
    public void goToUpdate(){
        ((MainActivity)getActivity()).loadFragment(new ProfileChangeFragment());
    }

}
