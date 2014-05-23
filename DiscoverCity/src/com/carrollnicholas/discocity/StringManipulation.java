package com.carrollnicholas.discocity;

/**
 * Created by Nicholas on 16/05/2014.
 */
/*
Nicholas Carroll
59578358
*/

import android.util.Log;

import java.io.*;
import java.util.*;
import java.text.Normalizer;

/*
This class is designed to remove stop words from a sentence that it has gotten
*/

class StopWords implements Cloneable, Serializable{
    String[] stopWordList;
    String[] inputSplit;
    String textInput;

    public StopWords(){}

    //equals method for canonical standard
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        StopWords s;
        try{
            s = (StopWords)o;
        }
        catch(ClassCastException e){
            return false;
        }
        if(stopWordList.equals(s.stopWordList) &&
                inputSplit.equals(s.inputSplit) &&
                textInput.equals(s.textInput)){
            return true;
        }
        else{
            return false;
        }
    }

    //This method fills us the stop word array with stop words for comparison, a small subset is currently in here
    public void fillStopWordList(){
        String[] stopWordList = {"the", "it", "or", "and", "at", "which", "on",
                "an", "also", "am", "me", "my", "we", "us", "our"};
        this.stopWordList = stopWordList;
    }

    //Setter method to retrieve the text to be modified
    public void setText(String textInput){
        this.textInput = textInput;
    }


    //Splitter method which splits the text the Getter method received.
    public void getSplitText(){
        inputSplit = textInput.split(" ");
    }

    //Contains method to check if a word is in the StopWordList
    public boolean contains(String textInput){
        for(int i = 0; i < stopWordList.length; i++){
            if(stopWordList[i].equals(textInput)){
                return true;
            }
        }
        return false;
    }

    //Method that checks the input against the StopWordList and removes stop words
    public String removeStopWords(){
        fillStopWordList();
        getSplitText();
        textInput = "";
        for(String s : inputSplit){
            if(contains(s)){
                textInput = textInput;
            }
            else{
                textInput =  textInput + s;
            }
        }
        return textInput;

    }


}

public class StringManipulation{
    public static String tagText;
    public String tagString(String tag){
        tag = normalText(tag);
        tag = removePunc(tag);
        tag = removeStops(tag);
        return "#" + tag;
    }

    public static String normalText(String textInput){
        textInput = textInput.toLowerCase();
        //Normalizes the text
        String s =  Normalizer.normalize(textInput, Normalizer.Form.NFD);
        //removes all nonstandard a-z characters
        textInput = s.replaceAll("[^\\p{ASCII}]", "");
        return textInput;
    }

    public static String removePunc(String textInput){
        //This regular expression removes all punctuation and replaces them with the empty string
        textInput = textInput.replaceAll("\\p{P}", "");
        return textInput;
    }

    public String removeStops(String textInput){
        StopWords s = new StopWords();
        s.setText(textInput);
        textInput = s.removeStopWords();
        return textInput;
    }

}