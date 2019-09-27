package com.example.android.sacco;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Abhi on 20 Jan 2018 020.
 */

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_MEMBER_NO = "MemberNo";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_MOBILE_NO = "MobileNo";
    private static final String KEY_ID_NUMBER = "IDNumber";
    private static final String KEY_MEMBER_ID = "MemberId";
    private static final String KEY_BIRTH_DATE = "BirthDate";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_CREATED_DATE = "CreatedDate";
    private static final String KEY_CONFIRMED = "Confirmed";
    private static final String KEY_NEXT_OF_KIN_NAME = "NextOfKinFullName";
    private static final String KEY_NEXT_OF_KIN_MOBILE = "NextOfKinMobileNo";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private Bitmap bx;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /*
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     */
    public void loginMember(Member m) {

        mEditor.putString(KEY_MEMBER_NO, m.getMemberNo());
        mEditor.putString(KEY_FULL_NAME, m.getFullName());
        mEditor.putString(KEY_MEMBER_ID, m.getMemberId());
        mEditor.putString(KEY_BIRTH_DATE, m.getBirthDate());
        mEditor.putString(KEY_CREATED_DATE, m.getCreatedDate());
        mEditor.putString(KEY_EMAIL, m.getEmail());
        mEditor.putString(KEY_ID_NUMBER, m.getIDNumber());
        mEditor.putString(KEY_MOBILE_NO, m.getMobileNo());
        mEditor.putString(KEY_ADDRESS, m.getAddress());
        mEditor.putLong(KEY_CONFIRMED, m.getConfirmed());
        mEditor.putString(KEY_NEXT_OF_KIN_NAME, m.getNextOfKinFullName());
        mEditor.putString(KEY_NEXT_OF_KIN_MOBILE, m.getNextOfKinMobileNo());
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */

    public void setBitmap(Bitmap bv)
    {
        bx = bv;
    }

    public Bitmap UserBitmap()
    {
        return bx;
    }

    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_MEMBER_NO, KEY_EMPTY));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    public Member getMemberDetails()
    {
        if(!isLoggedIn())
        {
            return null;
        }

        Member m = new Member();
        m.setMemberNo(mPreferences.getString(KEY_MEMBER_NO, KEY_EMPTY));
        m.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        m.setMemberId(mPreferences.getString(KEY_MEMBER_ID, KEY_EMPTY));
        m.setBirthDate(mPreferences.getString(KEY_BIRTH_DATE, KEY_EMPTY));
        m.setCreatedDate(mPreferences.getString(KEY_CREATED_DATE, KEY_EMPTY));
        m.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        m.setIDNumber(mPreferences.getString(KEY_ID_NUMBER, KEY_EMPTY));
        m.setMobileNo(mPreferences.getString(KEY_MOBILE_NO, KEY_EMPTY));
        m.setAddress(mPreferences.getString(KEY_ADDRESS, KEY_EMPTY));
        m.setConfirmed(mPreferences.getLong(KEY_CONFIRMED, 0));
        m.setNextOfKinFullName(mPreferences.getString(KEY_NEXT_OF_KIN_NAME, KEY_EMPTY));
        m.setNextOfKinMobileNo(mPreferences.getString(KEY_NEXT_OF_KIN_MOBILE, KEY_EMPTY));
        return m;
    }
    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}
