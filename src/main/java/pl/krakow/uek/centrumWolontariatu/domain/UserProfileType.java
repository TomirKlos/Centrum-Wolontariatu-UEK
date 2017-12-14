package pl.krakow.uek.centrumWolontariatu.domain;

import java.io.Serializable;

/**
 * Created by MSI DRAGON on 2017-12-11.
 */


public enum UserProfileType implements Serializable{
    USER("USER"),
    DBA("DBA"),
    ADMIN("ADMIN");

    String userProfileType;

    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType(){
        return userProfileType;
    }

}
