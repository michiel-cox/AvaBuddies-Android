package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.models.responses.tag.TagListResponse;
import com.projectsoa.avabuddies.data.models.responses.tag.TagResponse;
import com.projectsoa.avabuddies.data.services.TagService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class TagRepository {
    private TagService tagService;

    public TagRepository(TagService tagService){ this.tagService = tagService; }

    public Single<List<Tag>> getList(){
        return tagService.fetchList().map(TagListResponse -> {
            List<Tag> tags = new ArrayList<>();
            for(TagResponse tag : TagListResponse.tags){
                tags.add(new Tag(tag));
            }
            return tags;
        });
    }

}
