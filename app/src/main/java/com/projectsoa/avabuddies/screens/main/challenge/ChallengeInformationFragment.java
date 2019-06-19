package com.projectsoa.avabuddies.screens.main.challenge;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Challenge;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;

public class ChallengeInformationFragment extends BaseFragment {

    private static final String ARG_CHALLENGE = "challenge";
    @Inject
    protected ChallengeViewModel viewModel;

    @BindView(R.id.challengeTitle)
    protected TextView name;
    @BindView(R.id.challengeDescription)
    protected TextView description;
    @BindView(R.id.challengeImage)
    protected ImageView thumbnail;

    protected Challenge challenge;

    public static ChallengeInformationFragment newInstance(Challenge challenge) {
        ChallengeInformationFragment fragment = new ChallengeInformationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CHALLENGE, Parcels.wrap(challenge));
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challenge = Parcels.unwrap(getArguments().getParcelable(ARG_CHALLENGE));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getViewModel(ChallengeViewModel.class);

        name.setText(this.challenge.getTitle());
        description.setText(this.challenge.getDescription());
        boolean hasImage = false;
        if (challenge.getImage() != null && !challenge.getImage().isEmpty()) {
            try {
                byte[] imageByteArray = Base64.decode(challenge.getImage(), Base64.DEFAULT);
                Glide.with(getContext())
                        .asBitmap()
                        .apply(RequestOptions.circleCropTransform())
                        .load(imageByteArray)
                        .into(thumbnail);
                hasImage = true;
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_challenge;
    }
}
