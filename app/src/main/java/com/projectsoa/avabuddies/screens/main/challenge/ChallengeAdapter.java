package com.projectsoa.avabuddies.screens.main.challenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.data.models.Challenge;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {

    private final Context context;
    private final ChallengeInteractionListener listener;
    @Inject
    private List<Challenge> challengeList;

    public ChallengeAdapter(Context context, ChallengeInteractionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_challenge, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Challenge challenge;
        if(challengeList  != null) {
            challenge = challengeList.get(position);
        }else{

        }
    }

    @Override
    public int getItemCount() {
        if(challengeList != null){
            return challengeList.size();
        }else{
            return 0;
        }
    }

    private class ChallengeInteractionListener {
        public void onUserListInteract(Challenge challenge) {
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public Challenge challenge;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textName);


            if (listener != null) {
                view.setOnClickListener(v -> listener.onUserListInteract(challenge));
            }
        }
    }
}
