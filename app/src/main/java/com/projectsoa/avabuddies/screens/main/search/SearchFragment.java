package com.projectsoa.avabuddies.screens.main.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.profile.ProfileFragment;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileFragment;
import com.projectsoa.avabuddies.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SearchFragment extends BaseFragment implements UsersAdapter.UsersInteractionListener, SearchView.OnQueryTextListener {

    public SearchFragment() {

    }

    @BindView(R.id.search)
    protected SearchView searchView;

    @BindView(R.id.list)
    protected RecyclerView recyclerView;

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected LoginRepository loginRepository;

    @Inject
    protected Utils utils;

    protected UsersAdapter usersAdapter;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_search;
    }
/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }
*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usersAdapter = new UsersAdapter(getContext(),  this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        userRepository.getList().subscribe(users -> {
            getActivity().runOnUiThread(() -> {
                usersAdapter.setUserList(users);
                usersAdapter.notifyDataSetChanged();
            });
        }, throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.error_users))));

        searchView.setOnQueryTextListener(this);
        searchView.setOnClickListener(v -> searchView.setIconified(false));
    }

    @Override
    public boolean onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onUserListInteract(User user) {
        if(loginRepository.isLoggedIn() && loginRepository.getLoggedInUser().getUser().getId().equals(user.getId())){
            ((MainActivity)getActivity()).loadFragment(new ProfileFragment());
            return;
        }
        // Go to profile
        PublicProfileFragment fragment = PublicProfileFragment.newInstance(user);
        ((MainActivity)getActivity()).loadFragment(fragment);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        usersAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        usersAdapter.getFilter().filter(query);
        return false;
    }
}
