package com.projectsoa.avabuddies.screens.main.tag;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final TagsInteractionListener listener;
    @Inject
    protected LoginRepository loginRepository;
    private List<Tag> tagList;
    private List<Tag> selectedTags;
    private List<Tag> tagListFiltered;

    public TagsAdapter(Context context, TagsInteractionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }


    public void setSelectedTags(List<Tag> selectedTags) {
        this.selectedTags = selectedTags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tag, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Tag tag;

        if(tagListFiltered  != null) {
            tag = tagListFiltered.get(position);
        }else{
            tag = tagList.get(position);
        }

        holder.tag = tag;
        holder.name.setText(tag.getName());
        holder.privateName.setText(tag.isPrivate ? "Prive" : "");

        boolean isSelected = false;
        for(Tag selectTag : this.selectedTags){
            if(selectTag.get_id().equals(tag.get_id())){
                isSelected = true;
            }
        }
        if(isSelected){
            holder.name.setBackgroundColor(Color.GRAY);
        }else{
            holder.name.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        if(tagListFiltered != null){
            return tagListFiltered.size();
        }else if(tagList != null){
            return tagList.size();
        }else{
            return 0;
        }
    }

    public List<Tag> filter(CharSequence charSequence){

        List<Tag> filteredList;
        String charString = charSequence.toString();
        if(charString.isEmpty()){
            filteredList = tagList;
        } else {
            filteredList = new ArrayList<>();
            for(Tag tag : tagList){
                if(tag.getName().toLowerCase().contains(charString.toLowerCase())){
                    filteredList.add(tag);
                }
            }
        }
        return filteredList;
    }
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = TagsAdapter.this.filter(charSequence);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tagListFiltered = (ArrayList<Tag>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface TagsInteractionListener {
        void onUserListInteract(Tag tag);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView privateName;
        public Tag tag;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.challengeName);
            privateName = view.findViewById(R.id.privateName);

            if (listener != null) {
                view.setOnClickListener(v -> listener.onUserListInteract(tag));
            }
        }
    }

}