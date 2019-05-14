package com.projectsoa.avabuddies.screens.main.tag;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class TagsFragment extends BaseFragment {

    @Inject
    protected TagRepository tagRepository;
    @Inject
    protected UserRepository userRepository;
    @Inject
    protected LoginRepository loginRepository;
    @Inject
    protected Utils utils;

    protected List<Tag> selectedTagList;
    protected TagsAdapter tagsAdapter;
    protected List<Tag> tagList;

    public TagsFragment() {

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

    private void OnAddChip() {
        for (Tag tag : tagList) {
            Chip chip = new Chip(getContext());
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tag isfound = null;
                    for (Tag tagInList : selectedTagList) {
                        if (tagInList.get_id().equals(tag.get_id())) {
                            isfound = tagInList;
                        }
                    }

                    if (isfound != null) {
                        selectedTagList.remove(isfound);
                        chip.setChipBackgroundColorResource(R.color.colorGray);
                    } else {
                        selectedTagList.add(tag);
                        chip.setChipBackgroundColorResource(R.color.colorPrimary);
                    }
                }
            });
            chipGroup.addView(chip);

            chip.setText(tag.getName());
            chip.setTextColor(getResources().getColor(R.color.colorWhite));
            chip.setChipBackgroundColorResource(R.color.colorGray);

            for (Tag selectTag : selectedTagList) {
                if (selectTag.get_id().equals(tag.get_id())) {
                    chip.setChipBackgroundColorResource(R.color.colorPrimary);
                }
            }

            chip.setChipIcon(null);

        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_tags;
    }

    @OnClick(R.id.btn_saveTags)
    public void saveTags() {
        User user = loginRepository.getLoggedInUser().getUser();
        user.setTags(selectedTagList);
        userRepository.update(user).subscribe(() -> {
                },
                throwable -> getActivity().runOnUiThread(() -> utils.showToastError(getString(R.string.something_went_wrong))));
    }


}
