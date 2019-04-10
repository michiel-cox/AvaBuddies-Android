package com.projectsoa.avabuddies.screens.main.profile;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.utils.Utils;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;


public class ProfileChangeFragment extends BaseFragment {

    protected User user;
    protected ProfileChangeViewModel viewModel;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected Utils utils;
    @BindView(R.id.aboutMe)
    protected EditText aboutme;
    @BindView(R.id.location)
    protected Switch location;
    protected User updatedUser;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user = loginRepository.getLoggedInUser().getUser();

        aboutme.setText(user.getAboutme());
        location.setChecked(user.isSharelocation());

        viewModel = getViewModel(ProfileChangeViewModel.class);
    }

    @OnClick(R.id.updateProfile)
    public void updateProfile(){
        this.updatedUser = this.user;
        this.updatedUser.setAboutme(aboutme.getText());
        this.updatedUser.setSharelocation(location.isChecked());
        this.userRepository.update(updatedUser);
    }
    @Override
    protected int layoutRes() { return R.layout.fragment_profile_change; }
}
