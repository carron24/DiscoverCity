package com.carrollnicholas.discocity;

import android.app.Activity;
import android.location.Location;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Nicholas on 27/05/2014.
 */
class JsonToInfo{
    public Activity activity;
    private String tagText;
    private static final String TAG = "JSONtoInfo";


    public JsonToInfo(Activity ac, String tagText){
        this.activity = ac;
        this.tagText = tagText;
    }


    public ArrayList<Double> getDistances(ArrayList<ImageDetails> id, Location loc){
        ArrayList<Double> distances = new ArrayList<Double>();
        for (int i = 0; i < id.size(); i++) {
            JSONObject json;
            ImageDetails image = id.get(i);

            double longi = Double.parseDouble(image.longiData);
            double lati =  Double.parseDouble(image.latiData);
            Location loc1 = new Location("point b");

            loc1.setLatitude(lati);
            loc1.setLongitude(longi);


            double distance = loc.distanceTo(loc1);
            distances.add(distance);
            id.get(i).distance = distance;
            id.get(i).distanceString = distance + "";
        }
        return distances;
    }


    public ArrayList<ImageDetails> bestSharpness(ArrayList<ImageDetails> id){

        ArrayList<ImageDetails> tempList = new ArrayList<ImageDetails>();
        Set<ImageDetails> s = new TreeSet<ImageDetails>(new Comparator<ImageDetails>() {

            @Override
            public int compare(ImageDetails o1, ImageDetails o2) {
                if(o1.equals(o2)) {
                    return 0;
                }
                else
                    return 1;
            }
        });
        s.retainAll(id);
        s.addAll(id);

        for(ImageDetails tmp : s){
            ImageDetails imageDetail = new ImageDetails();
            for(ImageDetails temp : id){
                if(temp.sharpness != null && tmp.sharpness != null) {
                    if (temp.equals(tmp)) {
                        if (temp.sharpCompare(imageDetail)) {
                            imageDetail = temp;
                        }

                    }
                }
            }


            if(!tempList.contains(imageDetail) && !imageDetail.tagText.equals(tagText)) {
                tempList.add(imageDetail);
            }

        }
        for(ImageDetails tmp1 : tempList) {
            Log.v("whatthehell: ", "DBcontents: " + tmp1.sharpness + " " + tmp1.tagText + " " + tagText);
        }
        return tempList;
    }

    protected ArrayList<ImageDetails> brightnessThreshold(ArrayList<ImageDetails> id){

        ArrayList<ImageDetails> tempList = new ArrayList<ImageDetails>();
        Log.v("threshold: ", id.size() + " before");
        for(int i = 0; i < id.size()-1; i++){
            if(id.get(i).brightness != null){
                if(Integer.parseInt(id.get(i).brightness) < 300 && Integer.parseInt(id.get(i).brightness) > 190)
                    tempList.add(id.get(i));
            }
        }
        Log.v("threshold: ", tempList.size() + " after");
        return tempList;
    }

    public ArrayList<ImageDetails> closestImages(ArrayList<ImageDetails> id, Location loc){
        ArrayList<ImageDetails> tempList = new ArrayList<ImageDetails>();
        id = brightnessThreshold(bestSharpness(id));
        ArrayList<Double> distances = getDistances(id, loc);
        Collections.sort(distances);
        for(ImageDetails temp : tempList){
            Log.v(TAG, ""+ temp.distance);
        }

        for(int i = 0; i < id.size()-1; i++){
            Log.v(TAG, id.get(i).brightness + " what");
            for(ImageDetails temp : id){
                if(distances.get(i) == temp.distance){
                    tempList.add(temp);
                }
            }
        }
        for(ImageDetails temp : tempList){
            Log.v(TAG, ""+ temp.distance);
        }

        displayReturnData(tempList);
        return tempList;
    }

    public JSONArray infoReturn(JSONArray jsonArray){
        JSONArray jsReturn = null;
        for (int i = 0; i < jsonArray.length()-1; i++) {
            JSONObject json;
            try {
                json = jsonArray.getJSONObject(i);
                ImageDetails jsonDetails = new ImageDetails(json);
                ImageReturn.id.add(jsonDetails);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return jsReturn;
    }

    public void displayReturnData(ArrayList<ImageDetails> id){
        Log.v("size", id.size() + "");
        for(ImageDetails tmp : id){
            Log.v("tempList: ", tmp.tagText);
        }
        for(int i = 0; i < 5; i++){
            tagReturn(id.get(i), i);
            descReturn(id.get(i), i);
            locReturn(id.get(i), i);
            imageReturn(id.get(i), i);

        }

    }


    public void tagReturn(ImageDetails id, int i){
        String type = id.tagText;
        int[] ids = {R.id.textView, R.id.textView4, R.id.textView7, R.id.textView10, R.id.textView13};
        TextView text = (TextView) this.activity.findViewById(ids[i]);
        text.setText(type);
    }

    public void descReturn(ImageDetails id, int i){

        String type = id.descText;
        int[] ids = {R.id.textView2, R.id.textView5, R.id.textView8, R.id.textView11, R.id.textView14};
        TextView text = (TextView) this.activity.findViewById(ids[i]);
        text.setText(type);
    }

    public void locReturn(ImageDetails id, int i){

        int[] ids = {R.id.textView3, R.id.textView6, R.id.textView9, R.id.textView12, R.id.textView15};
        TextView text = (TextView) this.activity.findViewById(ids[i]);
        DecimalFormat df2 = new DecimalFormat("###.##");
        String type = Double.valueOf(df2.format(id.distance)) + " metres away";
        text.setText(type);
    }

    public void imageReturn(ImageDetails id, int i){

        String type = id.imageLink;
        String s = "http://nicholascarroll.info:3000" + type;
        int[] ids = {R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5};
        new DownloadImageTask((ImageView)this.activity.findViewById(ids[i]))
                .execute(s);

    }
}


