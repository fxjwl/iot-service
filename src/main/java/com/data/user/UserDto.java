package com.data.user;

import org.springframework.data.annotation.Transient;

public class UserDto {

    private String name;

    private String avatar;

    private String password;

    private String mobile;

    private String jobNumber;

    private String wxcodeUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getWxcodeUrl() {
        return wxcodeUrl;
    }

    public void setWxcodeUrl(String wxcodeUrl) {
        this.wxcodeUrl = wxcodeUrl;
    }
}
