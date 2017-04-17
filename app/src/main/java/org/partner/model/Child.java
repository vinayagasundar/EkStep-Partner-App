package org.partner.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class to hold the Child Information from Genie
 *
 * @author vinayagasundar
 */

public class Child implements Parcelable {


    private int age;
    private String avatar;
    private String createdAt;
    private int day;
    private String gender;
    private String handle;
    private boolean isGroupUser;
    private String language;
    private int month;
    private int standard;
    private String uid;


    private String fullName;
    private String studentId;
    private String schoolId;

    public Child() {

    }

    protected Child(Parcel in) {
        age = in.readInt();
        avatar = in.readString();
        createdAt = in.readString();
        day = in.readInt();
        gender = in.readString();
        handle = in.readString();
        isGroupUser = in.readByte() != 0;
        language = in.readString();
        month = in.readInt();
        standard = in.readInt();
        uid = in.readString();
        fullName = in.readString();
        studentId = in.readString();
        schoolId = in.readString();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public boolean isIsGroupUser() {
        return isGroupUser;
    }

    public void setIsGroupUser(boolean isGroupUser) {
        this.isGroupUser = isGroupUser;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(avatar);
        dest.writeString(createdAt);
        dest.writeInt(day);
        dest.writeString(gender);
        dest.writeString(handle);
        dest.writeByte((byte) (isGroupUser ? 1 : 0));
        dest.writeString(language);
        dest.writeInt(month);
        dest.writeInt(standard);
        dest.writeString(uid);
        dest.writeString(fullName);
        dest.writeString(studentId);
        dest.writeString(schoolId);
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };
}
