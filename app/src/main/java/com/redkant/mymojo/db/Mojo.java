package com.redkant.mymojo.db;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mojo implements Serializable {

    private int mID;
    private String mCreateDate;
    private float mKetoNumber;
    private float mSugarNumber;
    private float mWeight;

    public Mojo() {

    }

    public Mojo(String createDate, float ketoNumber) {
        mCreateDate = createDate;
        mKetoNumber = ketoNumber;
    }

    public Mojo(String createDate, float ketoNumber, float sugarNumber) {
        this(createDate, ketoNumber);
        mSugarNumber = sugarNumber;
    }

    public Mojo(String createDate, float ketoNumber, float sugarNumber, float weight) {
        this(createDate, ketoNumber, sugarNumber);
        mWeight = weight;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String createDate) {
        mCreateDate = createDate;
    }

    public float getKetoNumber() {
        return mKetoNumber;
    }

    public void setKetoNumber(float ketoNumber) {
        mKetoNumber = ketoNumber;
    }

    public float getSugarNumber() {
        return mSugarNumber;
    }

    public void setSugarNumber(float sugarNumber) {
        mSugarNumber = sugarNumber;
    }

    public float getWeight() {
        return mWeight;
    }

    public void setWeight(float weight) {
        mWeight = weight;
    }

    @Override
    public String toString() {
        return this.mCreateDate + "; Keto:" + this.mKetoNumber;
    }
}
