package com.projectsoa.avabuddies.screens.main.challenge;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.data.models.Challenge;
import com.projectsoa.avabuddies.data.repositories.ChallengeRepository;

import java.util.List;

import javax.inject.Inject;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {

    private final Context context;
    private final ChallengeInteractionListener listener;
    @Inject
    protected ChallengeRepository challengeRepository;
    private List<Challenge> challengelist;

    public ChallengeAdapter(Context context, ChallengeInteractionListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public void setChallengeList(List<Challenge> challengelist){this.challengelist = challengelist;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_challenge,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Challenge challenge;
        challenge = challengelist.get(position);

        holder.challenge = challenge;
        holder.name.setText(challenge.getTitle());
        boolean hasImage = false;
        if (challenge.getImage() != null && !challenge.getImage().isEmpty()) {
            try {
                byte[] imageByteArray = Base64.decode(challenge.getImage(), Base64.DEFAULT);
                Glide.with(context)
                        .asBitmap()
                        .apply(RequestOptions.circleCropTransform())
                        .load(imageByteArray)
                        .into(holder.thumbnail);
                hasImage = true;
            } catch (IllegalArgumentException ignored) {

            }
        }
        holder.thumbnail.setVisibility(hasImage ? View.VISIBLE : View.INVISIBLE);
    }

    public interface ChallengeInteractionListener {
        void onUserListInteract(Challenge challenge);
    }

    @Override
    public int getItemCount() {
        return challengelist == null ? 0 : challengelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final ImageView thumbnail;
        public Challenge challenge;
        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.challengeName);
            thumbnail = view.findViewById(R.id.imageThumbnail);

            if (listener != null) {
                view.setOnClickListener(v -> listener.onUserListInteract(challenge));
            }
        }
    }


}


