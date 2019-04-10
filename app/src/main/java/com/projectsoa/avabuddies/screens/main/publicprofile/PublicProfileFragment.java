package com.projectsoa.avabuddies.screens.main.publicprofile;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.utils.Utils;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class PublicProfileFragment extends BaseFragment {
    private static final String ARG_USER = "userId";

    @Inject
    protected LoginRepository loginRepository;

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected FriendRepository friendRepository;


    protected PublicProfileViewModel viewModel;

    private User user;

    @BindView(R.id.buttonRequest)
    protected Button buttonRequest;

    @BindView(R.id.buttonRequestCancel)
    protected Button buttonRequestCancel;

    @BindView(R.id.PublicName)
    protected TextView name;
    @BindView(R.id.PublicEmail)
    protected TextView email;
    @BindView(R.id.PublicInfo)
    protected TextView info;
    @BindView(R.id.publicProfile)
    protected ImageView profile;

    @Inject
    protected Utils utils;

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
        viewModel= getViewModel(PublicProfileViewModel.class);
        hideFriendRequest();

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

        name.setText(user.getName());
        email.setText(user.getEmail());
        info.setText(user.getAboutme());

        friendRepository.getConnectionStatus(user.getId()).subscribe(connectionStatus -> {
            getActivity().runOnUiThread(() -> updateFriendRequest(connectionStatus));
        }, throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }


    private void hideFriendRequest(){
        buttonRequest.setVisibility(View.GONE);
        buttonRequestCancel.setVisibility(View.GONE);
    }
    private void updateFriendRequest(FriendRepository.ConnectionStatus status){
        switch (status) {
            case SEND:
                buttonRequestCancel.setVisibility(View.VISIBLE);
                break;
            case UNKNOWN:
                buttonRequest.setVisibility(View.VISIBLE);
                break;
        }

    }

    @OnClick(R.id.buttonRequest)
    public void onFriendRequest(){
        hideFriendRequest();
        friendRepository.request(user.getId()).subscribe(() -> {
            getActivity().runOnUiThread(() -> {
                utils.showToastMessage(getString(R.string.friend_request_send));
                updateFriendRequest(FriendRepository.ConnectionStatus.SEND);
            });
        }, throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));

    }

    @OnClick(R.id.buttonRequestCancel)
    public void onFriendRequestCancel(){
        hideFriendRequest();
        friendRepository.request(user.getId()).subscribe(() -> {
            getActivity().runOnUiThread(() -> {
                utils.showToastMessage(getString(R.string.friend_request_cancel));
                updateFriendRequest(FriendRepository.ConnectionStatus.UNKNOWN);
            });
        }, throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }
}
