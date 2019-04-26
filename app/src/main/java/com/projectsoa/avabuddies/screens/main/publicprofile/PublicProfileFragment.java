package com.projectsoa.avabuddies.screens.main.publicprofile;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.qrshow.QRShowFragment;
import com.projectsoa.avabuddies.utils.Utils;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PublicProfileFragment extends BaseFragment {
    private static final String ARG_USER = "user";

    @Inject
    protected LoginRepository loginRepository;

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected FriendRepository friendRepository;


    protected PublicProfileViewModel viewModel;
    @BindView(R.id.button_request)
    protected Button buttonRequest;
    @BindView(R.id.button_request_cancel)
    protected Button buttonRequestCancel;

    @BindView(R.id.button_deny)
    protected Button buttonDeny;

    @BindView(R.id.button_accept)
    protected Button buttonAccept;

    @BindView(R.id.button_validate)
    protected Button buttonValidate;

    @BindView(R.id.chip_group)
    protected ChipGroup chipGroup;
    @BindView(R.id.public_name)
    protected TextView name;
    @BindView(R.id.public_email)
    protected TextView email;
    @BindView(R.id.public_info)
    protected TextView info;
    @BindView(R.id.public_profile)
    protected ImageView profile;
    @Inject
    protected Utils utils;
    private User user;

    public PublicProfileFragment() {

    }

    public static PublicProfileFragment newInstance(User user) {
        PublicProfileFragment fragment = new PublicProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable(ARG_USER));
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_public_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getViewModel(PublicProfileViewModel.class);
        hideFriendRequest();

        if (!user.getImage().isEmpty()) {
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

        name.setText(user.getName());
        email.setText(user.getEmail());

        info.setText(user.getAboutme());

        for (Tag tag : user.getTags()) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, null));
            chip.setChipIcon(null);
            chipGroup.addView(chip);
        }

        friendRepository.getConnectionStatus(user.getId()).subscribe(connectionStatus -> {
            runOnUiThread(() -> updateFriendRequest(connectionStatus));
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }


    private void hideFriendRequest() {
        buttonRequest.setVisibility(View.GONE);
        buttonRequestCancel.setVisibility(View.GONE);
        buttonAccept.setVisibility(View.GONE);
        buttonDeny.setVisibility(View.GONE);
        buttonValidate.setVisibility(View.GONE);
    }

    private void updateFriendRequest(FriendRepository.ConnectionStatus status) {
        switch (status) {
            case SEND:
                buttonRequestCancel.setVisibility(View.VISIBLE);
                break;
            case UNKNOWN:
                buttonRequest.setVisibility(View.VISIBLE);
                break;
            case RECEIVED:
                buttonDeny.setVisibility(View.VISIBLE);
                buttonValidate.setVisibility(View.VISIBLE);
                break;
            case VALIDATED:
                buttonDeny.setVisibility(View.VISIBLE);
                buttonAccept.setVisibility(View.VISIBLE);
                break;
        }

    }

    @OnClick(R.id.button_request)
    public void onFriendRequest() {
        hideFriendRequest();
        friendRepository.request(user.getId()).subscribe(() -> {
            runOnUiThread(() -> {
                utils.showToastMessage(getString(R.string.friend_request_send));
                updateFriendRequest(FriendRepository.ConnectionStatus.SEND);
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));

    }

    @OnClick(R.id.button_request_cancel)
    public void onFriendRequestCancel() {
        hideFriendRequest();
        friendRepository.cancelRequest(user.getId()).subscribe(() -> {
            runOnUiThread(() -> {
                utils.showToastMessage(getString(R.string.friend_request_cancel));
                updateFriendRequest(FriendRepository.ConnectionStatus.UNKNOWN);
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }

    @OnClick(R.id.button_deny)
    public void onFriendRequestDeny() {
        hideFriendRequest();
        friendRepository.denyRequest(user.getId()).subscribe(() -> {
            runOnUiThread(() -> {
                utils.showToastMessage(getString(R.string.friend_request_deny));
                updateFriendRequest(FriendRepository.ConnectionStatus.UNKNOWN);
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }

    @OnClick(R.id.button_accept)
    public void onFriendRequestAccept() {
        friendRepository.acceptRequest(user.getId()).subscribe(() -> {
            utils.showToastMessage(getString(R.string.friend_request_accept));
            updateFriendRequest(FriendRepository.ConnectionStatus.ACCEPTED);
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }

    @OnClick(R.id.button_validate)
    public void showQR(){
        ((MainActivity) getActivity()).loadFragment(QRShowFragment.newInstance(user));
    }
}
