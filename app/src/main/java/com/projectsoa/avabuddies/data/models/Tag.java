package com.projectsoa.avabuddies.data.models;

import com.projectsoa.avabuddies.data.models.responses.tag.TagResponse;

import org.parceler.Parcel;

@Parcel
public class Tag {

    protected String _id;
    protected String name;
    public Boolean isPrivate = true;

    public Tag(){}

    public Tag(String id, String name, Boolean isPrivate) {
        _id = id;
        this.name = name;
        this.isPrivate = isPrivate;
    }

    public String get_id() {
        return _id;
    }

    public Tag (TagResponse response){
        _id = response._id;
        name = response.name;
    }

    public TagResponse tagToRespone(){
        TagResponse response = new TagResponse();
        response.name = name;
        response._id = _id;
        return response;
    }


    public String getName() {
        return name;
    }
    public Boolean getPrivate() { return isPrivate; }
}
