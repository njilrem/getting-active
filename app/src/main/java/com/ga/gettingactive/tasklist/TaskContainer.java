package com.ga.gettingactive.tasklist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskContainer implements Parcelable{
    private String title;
    private String description;
    private ArrayList<String> hashtags;
    private String tip;

    public TaskContainer(){

    }

    public TaskContainer(String title, String text, String tip){
        this.title = title;
        this.description = text;
        this.tip = tip;
    }

    public String getTip() { return tip; }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TaskContainer{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hashtags='" + hashtags + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        //write all properties to the parcel
        dest.writeString(title);

        dest.writeString(description);
        dest.writeString(tip);

        dest.writeStringArray(hashtags.toArray(new String[0]));
    }

    //constructor used for parcel
    public TaskContainer(Parcel parcel){
        this.title = parcel.readString() ;
        this.description = parcel.readString();
        this.tip = parcel.readString();
        this.hashtags = new ArrayList<>();
        hashtags.addAll(Arrays.asList(parcel.createStringArray()));
    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<TaskContainer> CREATOR = new Parcelable.Creator<TaskContainer>(){

        @Override
        public TaskContainer createFromParcel(Parcel parcel) {
            return new TaskContainer(parcel);
        }

        @Override
        public TaskContainer[] newArray(int size) {
            return new TaskContainer[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}
