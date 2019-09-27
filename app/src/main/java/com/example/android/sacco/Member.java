package com.example.android.sacco;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Member implements Serializable {
    String MemberId = "";
    String MemberNo = "";
    String FullName = "";
    String BirthDate = "";
    String MobileNo = "";
    String IDNumber = "";
    String Email = "";
    String Address = "";
    String CreatedDate = "";
    String NextOfKinFullName = "";
    String NextOfKinMobileNo = "";
    long Confirmed = 0;

    public Member() {
        MemberId = "";
        MemberNo = "";
        FullName = "";
        BirthDate = "";
        MobileNo = "";
        IDNumber = "";
        Email = "";
        Address = "";
        CreatedDate = "";
        NextOfKinFullName = "";
        NextOfKinMobileNo = "";
        Confirmed = 0;
    }

    public void setMemberId(String szvMemberId) {
        this.MemberId = szvMemberId;
    }
    public String getMemberId() {
        return MemberId;
    }

    public void setMemberNo(String szvMemberNo) {
        this.MemberNo = szvMemberNo;
    }
    public String getMemberNo() {
        return MemberNo;
    }

    public void setFullName(String szvFullName) {
        this.FullName = szvFullName;
    }
    public String getFullName() {
        return FullName;
    }

    public void setMobileNo(String szvMobileNo) {
        this.MobileNo = szvMobileNo;
    }

    public void setBirthDate(String szvBirthDate) {
        this.BirthDate = szvBirthDate;
    }
    public String getBirthDate() {
        return BirthDate;
    }

    public void setIDNumber(String szvIDNumber) {
        this.IDNumber = szvIDNumber;
    }
    public String getIDNumber() {
        return IDNumber;
    }

    public void setEmail(String szvEmail) {
        this.Email = szvEmail;
    }
    public String getEmail() {
        return Email;
    }

    public void setAddress(String szvAddress) {
        this.Address = szvAddress;
    }
    public String getAddress() {
        return Address;
    }

    public void setCreatedDate(String szvCreatedDate) {
        this.CreatedDate = szvCreatedDate;
    }
    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setConfirmed(long nvConfirmed) {
        this.Confirmed = nvConfirmed;
    }
    public long getConfirmed() {
        return Confirmed;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setNextOfKinFullName(String szvNextOfKinFullName) {
        this.NextOfKinFullName = szvNextOfKinFullName;
    }
    public String getNextOfKinFullName() {
        return NextOfKinFullName;
    }

    public void setNextOfKinMobileNo(String szvNextOfKinMobileNo) {
        this.NextOfKinMobileNo = szvNextOfKinMobileNo;
    }
    public String getNextOfKinMobileNo() {
        return NextOfKinMobileNo;
    }
}
