package com.aoslec.haezzo.Bean;

import com.kakao.usermgmt.response.model.User;

public class UserListBean {

    private String unumber;
    private String uimage;
    private String uemail;
    private String uage;
    private String ufm;
    private String unickname;
    private String uaddress;

    // login 용
    public UserListBean(String uemail) {
        this.uemail = uemail;
    }

    //helperlist 목록에 들어갈 것 다 부르기
    public UserListBean(String unumber, String uimage, String uemail, String uage, String ufm, String unickname, String uaddress) {
        this.unumber = unumber;
        this.uimage = uimage;
        this.uemail = uemail;
        this.uage = uage;
        this.ufm = ufm;
        this.unickname = unickname;
        this.uaddress = uaddress;
    }

    public String getUnumber() {
        return unumber;
    }

    public void setUnumber(String unumber) {
        this.unumber = unumber;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getUemail() { return uemail; }

    public void setUemail(String uemail) { this.uemail = uemail; }

    public String getUage() {
        return uage;
    }

    public void setUage(String uage) {
        this.uage = uage;
    }

    public String getUfm() {
        return ufm;
    }

    public void setUfm(String ufm) {
        this.ufm = ufm;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

}//Bean
