package com.projectsoa.avabuddies.screens.main.challenge;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Challenge;
import com.projectsoa.avabuddies.data.repositories.ChallengeRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChallengeFragment extends BaseFragment implements ChallengeAdapter.ChallengeInteractionListener{

    @BindView(R.id.list)
    protected RecyclerView recyclerView;
    @Inject
    protected ChallengeRepository challengeRepository;
    @Inject
    protected Utils utils;

    protected ChallengeAdapter challengeAdapter;
    protected List<Challenge> challengeList ;

    public ChallengeFragment(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        challengeAdapter = new ChallengeAdapter(getContext(), this);
        this.challengeList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setAdapter(challengeAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        challengeRepository.getList().subscribe(challenges -> {
            runOnUiThread(() -> {
                Iterator<Challenge> iterator = challenges.iterator();
                challengeAdapter.setChallengeList(challenges);
                challengeAdapter.notifyDataSetChanged();
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.error_challenges))));

    }

    @Override
    protected int layoutRes() { return R.layout.fragment_challenges; }

    @Override
    public void onUserListInteract(Challenge challenge) {
        // Go to profile
        ChallengeInformationFragment fragment = ChallengeInformationFragment.newInstance(challenge);
        ((MainActivity) getActivity()).loadFragment(fragment);
    }
}