package com.projectsoa.avabuddies.screens.main.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

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


public class ProfileChangeFragment extends BaseFragment {

    protected User user;
    protected ProfileChangeViewModel viewModel;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected Utils utils;
    @BindView(R.id.aboutme)
    protected EditText aboutme;
    @BindView(R.id.location)
    protected Switch location;
    @BindView(R.id.privacy)
    protected Switch privacy;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.user = loginRepository.getLoggedInUser().getUser();
        aboutme.setText(user.getAboutme());
        location.setChecked(user.isSharelocation());
        privacy.setChecked(user.isShareprofile());
        viewModel = getViewModel(ProfileChangeViewModel.class);
    }

    @OnClick(R.id.updateProfile)
    public void updateProfile() {
        this.user.setAboutme(aboutme.getText().toString());
        this.user.setSharelocation(location.isChecked());
        this.user.setShareprofile(privacy.isChecked());
        this.userRepository.update(user).subscribe(() -> {
                    ((MainActivity) getActivity()).loadFragment(new ProfileFragment());
                },
                throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }

    public void logout() {
        Intent intent = new Intent(getBaseActivity(), LoginActivity.class);
        intent.putExtra("logout", true);
        startActivity(intent);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_profile_change;
    }

    @OnClick(R.id.removeThisUser)
    public void removeThisUser() {

        new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.confirm_message))
                .setPositiveButton(getString(R.string.positive_button), (dialogInterface, i) -> {
            userRepository.delete(this.user).subscribe(() -> logout(), throwable -> {
                runOnUiThread(() -> utils.showToastError(getString(R.string.errorDeleteUser)));
            });
            dialogInterface.dismiss();
        }).setNegativeButton(getString(R.string.negative_button), (dialogInterface, i) -> {
            dialogInterface.dismiss();
        }).create().show();


    }
}
