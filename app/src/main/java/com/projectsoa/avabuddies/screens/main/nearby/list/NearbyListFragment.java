package com.projectsoa.avabuddies.screens.main.nearby.list;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileFragment;
import com.projectsoa.avabuddies.screens.main.search.UsersAdapter;
import com.projectsoa.avabuddies.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class NearbyListFragment extends BaseFragment implements UsersAdapter.UsersInteractionListener {


    @BindView(R.id.textSend)
    protected TextView textSend;
    @BindView(R.id.listSend)
    protected RecyclerView listSend;
    protected UsersAdapter adapterSend;

    @BindView(R.id.textReceived)
    protected TextView textReceived;
    @BindView(R.id.listReceived)
    protected RecyclerView listReceived;
    protected UsersAdapter adapterReceived;

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected LoginRepository loginRepository;

    @Inject
    protected FriendRepository friendRepository;

    @Inject
    protected Utils utils;


    public NearbyListFragment() {
        // Required empty public constructor
    }


    @Override
    protected int layoutRes() {
        return R.layout.fragment_nearby_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        adapterSend = new UsersAdapter(getContext(),  this);
        adapterReceived = new UsersAdapter(getContext(), this);

        textSend.setVisibility(View.GONE);
        textReceived.setVisibility(View.GONE);

        listSend.setVisibility(View.GONE);
        listReceived.setVisibility(View.GONE);

        initUsersList(listSend, adapterSend);
        initUsersList(listReceived, adapterReceived);

        friendRepository.getSendRequests().subscribe(users -> {
            runOnUiThread(() -> {
                this.updateSendRequests(users);
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));

        friendRepository.getReceivedRequests().subscribe(users -> {
            runOnUiThread(() -> {
                this.updateReceivedRequests(users);
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));


    }

    private void initUsersList(RecyclerView list, UsersAdapter adapter){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), linearLayoutManager.getOrientation());
        list.setAdapter(adapter);
        list.setLayoutManager(linearLayoutManager);
        list.addItemDecoration(dividerItemDecoration);
    }

    public void updateSendRequests(List<User> users){
        if(users.isEmpty()){
            listSend.setVisibility(View.GONE);
            textSend.setVisibility(View.GONE);
            return;
        }
        textSend.setVisibility(View.VISIBLE);
        listSend.setVisibility(View.VISIBLE);
        adapterSend.setUserList(users);
        adapterSend.notifyDataSetChanged();
    }

    public void updateReceivedRequests(List<User> users){
        if(users.isEmpty()){
            listReceived.setVisibility(View.GONE);
            textReceived.setVisibility(View.GONE);
            return;
        }
        listReceived.setVisibility(View.VISIBLE);
        textReceived.setVisibility(View.VISIBLE);
        adapterReceived.setUserList(users);
        adapterReceived.notifyDataSetChanged();
    }

    @Override
    public void onUserListInteract(User user) {
        PublicProfileFragment fragment = PublicProfileFragment.newInstance(user);
        ((MainActivity)getActivity()).loadFragment(fragment);
    }
}
