package com.redkant.mymojo.db;

import java.io.Serializable;

public class Body implements Serializable {
    private int mID;
    private String mCreateDate;
    private float mWeight;
    private float mArmHighLeft;
    private float mArmHighRight;
    private float mArmLowLeft;
    private float mArmLowRight;
    private float mChest;
    private float mChestUnder;
    private float mWaist;
    private float mHipsHigh;
    private float mHips;
    private float mThighLeft;
    private float mThighRight;
    private float mCalfLeft;
    private float mCalfRight;

    public Body() {
    }

    @Override
    public String toString() {
        return "Body{" +
                "mCreateDate='" + mCreateDate + '\'' +
                ", mWeight=" + mWeight +
                '}';
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

    public float getWeight() {
        return mWeight;
    }

    public void setWeight(float weight) {
        mWeight = weight;
    }

    public float getArmHighLeft() {
        return mArmHighLeft;
    }

    public void setArmHighLeft(float armHighLeft) {
        mArmHighLeft = armHighLeft;
    }

    public float getArmHighRight() {
        return mArmHighRight;
    }

    public void setArmHighRight(float armHighRight) {
        mArmHighRight = armHighRight;
    }

    public float getArmLowLeft() {
        return mArmLowLeft;
    }

    public void setArmLowLeft(float armLowLeft) {
        mArmLowLeft = armLowLeft;
    }

    public float getArmLowRight() {
        return mArmLowRight;
    }

    public void setArmLowRight(float armLowRight) {
        mArmLowRight = armLowRight;
    }

    public float getChest() {
        return mChest;
    }

    public void setChest(float chest) {
        mChest = chest;
    }

    public float getChestUnder() {
        return mChestUnder;
    }

    public void setChestUnder(float chestUnder) {
        mChestUnder = chestUnder;
    }

    public float getWaist() {
        return mWaist;
    }

    public void setWaist(float waist) {
        mWaist = waist;
    }

    public float getHipsHigh() {
        return mHipsHigh;
    }

    public void setHipsHigh(float hipsHigh) {
        mHipsHigh = hipsHigh;
    }

    public float getHips() {
        return mHips;
    }

    public void setHips(float hips) {
        mHips = hips;
    }

    public float getThighLeft() {
        return mThighLeft;
    }

    public void setThighLeft(float thighLeft) {
        mThighLeft = thighLeft;
    }

    public float getThighRight() {
        return mThighRight;
    }

    public void setThighRight(float thighRight) {
        mThighRight = thighRight;
    }

    public float getCalfLeft() {
        return mCalfLeft;
    }

    public void setCalfLeft(float calfLeft) {
        mCalfLeft = calfLeft;
    }

    public float getCalfRight() {
        return mCalfRight;
    }

    public void setCalfRight(float calfRight) {
        mCalfRight = calfRight;
    }
}
