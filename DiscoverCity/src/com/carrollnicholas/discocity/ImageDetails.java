package com.carrollnicholas.discocity;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nicholas on 14/04/2014.
 */
public class ImageDetails implements Parcelable {
    public String tagText;
    public String descText;
    public String imageLink;
    public String longiData;
    public String latiData;
    public double distance;
    public String distanceString;

    // Constructor
    public ImageDetails(JSONObject json){
        try {
            tagText = json.getString("tagText");
            descText = json.getString("descText");
            imageLink = json.getString("image");
            longiData = json.getString("longitude");
            latiData = json.getString("latitude");
            distanceString = "";



        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
    // Getter and setter methods

    public void toDistanceString(){
        this.distanceString = this.distance + "";
    }
    // Parcelling part
    public ImageDetails(Parcel in){
        String[] data = new String[6];
        in.readStringArray(data);
        tagText = data[0];
        descText = data[1];
        imageLink = data[2];
        longiData = data[3];
        latiData = data[4];
        distanceString = data[5];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {tagText, descText, imageLink,
                               longiData, latiData, distanceString});
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