package com.projectsoa.avabuddies.screens.main.challenge;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Challenge;
import com.projectsoa.avabuddies.data.repositories.ChallengeRepository;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChallengeFragment extends BaseFragment implements ChallengeAdapter.ChallengeInteractionListener{

    @BindView(R.id.searchTags)
    protected SearchView searchView;
    @BindView(R.id.tagList)
    protected RecyclerView recyclerView;
    @Inject
    protected ChallengeRepository challengeRepository;


    protected ChallengeAdapter challengeAdapter;
    protected List<Challenge> challengeList ;

    public ChallengeFragment(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        challengeAdapter = new ChallengeAdapter(getContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setAdapter(ChallengeAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ChallengeRepository.getList().subscribe(Challenges -> {
            runOnUiThread(() -> {
                Iterator<Challenge> iterator = tags.iterator();
                ChallengeAdapter.setTagList(tags);
                ChallengeAdapter.notifyDataSetChanged();
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.error_tags))));

        searchView.setOnQueryTextListener(this);
        searchView.setOnClickListener(v -> searchView.setIconified(false));
    }

    @Override
    protected int layoutRes() { return .layout.fragment_tags; }

    @Override
    public void onUserListInteract(Challenge challenge) {

        ChallengeAdapter.notifyDataSetChanged();
    }

}
