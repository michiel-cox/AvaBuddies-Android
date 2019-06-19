package com.projectsoa.avabuddies.data.models;

import com.projectsoa.avabuddies.data.models.responses.challengee.ChallengeResponse;

import org.parceler.Parcel;

@Parcel
public class Challenge {

    protected String _id;
    protected String title;
    protected String description;
    protected String image;

    public Challenge(){}

    public Challenge(String _id, String title, String description, String image) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.image = image;
    }
    public Challenge(ChallengeResponse challengeResponse){
        this._id = challengeResponse._id;
        this.title = challengeResponse.title;
        this.description = challengeResponse.description;
        this.image = challengeResponse.image;
    }

    public String get_id() { return _id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getImage() { return image; }
}
