package com.projectsoa.avabuddies.screens.main.search;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.data.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final UsersInteractionListener listener;
    private List<User> userList;
    private List<User> userListFiltered;

    public UsersAdapter(Context context, UsersInteractionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        this.userListFiltered = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        User user = userListFiltered.get(position);
        holder.user = user;
        holder.name.setText(user.getName());
        holder.detail.setText(user.getEmail());

        boolean hasImage = false;
        if (!user.getImage().isEmpty()) {
            try {
                byte[] imageByteArray = Base64.decode(user.getImage(), Base64.DEFAULT);
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

    @Override
    public int getItemCount() {
        return userList == null || userListFiltered == null ? 0 : userListFiltered.size();
    }

    public List<User> filter(CharSequence charSequence) {

        List<User> filteredList;
        String charString = charSequence.toString();
        if (charString.isEmpty()) {
            filteredList = userList;
        } else {
            filteredList = new ArrayList<>();
            for (User user : userList) {

                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                if (user.getName().toLowerCase().contains(charString.toLowerCase()) || user.getEmail().contains(charSequence)) {
                    filteredList.add(user);
                }
            }
        }

        return filteredList;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = UsersAdapter.this.filter(charSequence);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userListFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<User> getFilteredUsers() {
        return userListFiltered;
    }

    public interface UsersInteractionListener {
        void onUserListInteract(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name, detail;
        public final ImageView thumbnail;
        public User user;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textName);
            detail = view.findViewById(R.id.textDetail);
            thumbnail = view.findViewById(R.id.imageThumbnail);

            if (listener != null) {
                view.setOnClickListener(v -> listener.onUserListInteract(user));
            }
        }
    }
}
