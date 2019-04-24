package com.projectsoa.avabuddies.screens.main.tag;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.TagRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.utils.Utils;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TagsFragment extends BaseFragment implements TagsAdapter.TagsInteractionListener{

    @BindView(R.id.tagList)
    protected RecyclerView recyclerView;
    @Inject
    protected TagRepository tagRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected Utils utils;

    protected TagsAdapter tagsAdapter;
    protected List<Tag> tagList ;

    public TagsFragment(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = loginRepository.getLoggedInUser().getUser();
        tagsAdapter = new TagsAdapter(getContext(), this);
        tagsAdapter.setSelectedTags(user.getTags());
        this.tagList = user.getTags();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setAdapter(tagsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        tagRepository.getList().subscribe(tags -> {
            runOnUiThread(() -> {
                Iterator<Tag> iterator = tags.iterator();
                tagsAdapter.setTagList(tags);
                tagsAdapter.notifyDataSetChanged();
            });
        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.error_tags))));

    }

    @Override
    protected int layoutRes() { return R.layout.fragment_tags; }

    @Override
    public void onUserListInteract(Tag tag) {

        Tag isfound = null;
        for(Tag tagInList : tagList){
            if(tagInList.get_id().equals(tag.get_id())){
                isfound = tagInList;
            }
        }

        if(isfound != null){
            tagList.remove(isfound);
        }else{
            tagList.add(tag);
        }

        tagsAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.btn_saveTags)
    public void saveTags(){
        User user = loginRepository.getLoggedInUser().getUser();
        user.setTags(tagList);
        userRepository.update(user).subscribe(() -> {
                },
                throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }



}
