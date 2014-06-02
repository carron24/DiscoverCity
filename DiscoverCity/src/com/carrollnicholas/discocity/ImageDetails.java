package com.carrollnicholas.discocity;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Nicholas on 14/04/2014.
 */
public class ImageDetails implements Parcelable {
    private String tagText;
    private String descText;
    private String imageLink;
    public String longiData;
    public String latiData;
    private String brightness;
    private String sharpness;
    private String contrast;
    private double distance;
    private String distanceString;

    // Constructor
    public ImageDetails(JSONObject json){
        try {
            tagText = json.getString("tagText");
            descText = json.getString("descText");
            imageLink = json.getString("image");
            longiData = json.getString("longitude");
            latiData = json.getString("latitude");
            brightness = json.getString("brightness");
            sharpness = json.getString("sharpness");
            contrast = json.getString("contrast");
            distanceString = "";



        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public ImageDetails(){
        tagText = "null";
        brightness = "0";
        sharpness = "0";
        distanceString = "";
    }
    // Getter and setter methods

    public String getTagText(){
        return tagText;
    }
    public String getDescText(){
        return descText;
    }
    public String getImageLink(){
        return imageLink;
    }
    public String getLongiData(){
        return longiData;
    }
    public String getLatiData(){
        return latiData;
    }
    public String getBrightness(){
        return brightness;
    }
    public String getSharpness(){
        return sharpness;
    }
    public double getDistance(){
        return distance;
    }
    public String getDistanceString(){
        return distanceString;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public void setDistanceString(String distanceString){
        this.distanceString = distanceString;
    }


    public boolean equals(ImageDetails id){
        return this.tagText.equals(id.tagText);
    }

    public int compare(ImageDetails o1, ImageDetails o2) {

        return 0;
    }

    public boolean sharpCompare(ImageDetails id){

        return Integer.parseInt(this.sharpness) > Integer.parseInt(id.sharpness);
    }


    public void toDistanceString(){
        this.distanceString = this.distance + "";
    }
    // Parcelling part
    public ImageDetails(Parcel in){
        String[] data = new String[9];
        in.readStringArray(data);
        tagText = data[0];
        descText = data[1];
        imageLink = data[2];
        longiData = data[3];
        latiData = data[4];
        brightness = data[5];
        sharpness = data[6];
        contrast = data[7];
        distanceString = data[8];

    }


    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {tagText, descText, imageLink,
                               longiData, latiData, brightness,sharpness,contrast, distanceString});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ImageDetails createFromParcel(Parcel in) {
            return new ImageDetails(in);
        }

        public ImageDetails[] newArray(int size) {
            return new ImageDetails[size];
        }
    };

}