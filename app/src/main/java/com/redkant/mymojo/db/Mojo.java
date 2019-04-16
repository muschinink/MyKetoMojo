package com.redkant.mymojo.db;

import java.io.Serializable;

public class Mojo implements Serializable {

    private int mID;
    private String mCreateDate;
    private float mKetoNumber;
    private int mGlucoseNumber;
    private float mWeight;

    public Mojo() {

    }

    public Mojo(String createDate, float ketoNumber) {
        mCreateDate = createDate;
        mKetoNumber = ketoNumber;
    }

    public Mojo(String createDate, float ketoNumber, int glucoseNumber) {
        this(createDate, ketoNumber);
        mGlucoseNumber = glucoseNumber;
    }

    public Mojo(String createDate, float ketoNumber, int glucoseNumber, float weight) {
        this(createDate, ketoNumber, glucoseNumber);
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

    public int getGlucoseNumber() {
        return mGlucoseNumber;
    }

    public void setGlucoseNumber(int glucoseNumber) {
        mGlucoseNumber = glucoseNumber;
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
