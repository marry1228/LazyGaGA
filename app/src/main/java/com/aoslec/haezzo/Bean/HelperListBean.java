package com.aoslec.haezzo.Bean;

public class HelperListBean {

    private String unumber;
    private String uimage;
    private String uage;
    private String ufm;
    private String unickname;
    private String uaddress;
    private String hgaga;
    private String hrating;

    //helperlist 목록에 들어갈 것 다 부르기
    public HelperListBean(String unumber, String uimage, String uage, String ufm, String unickname, String uaddress, String hgaga, String hrating) {
        this.unumber = unumber;
        this.uimage = uimage;
        this.uage = uage;
        this.ufm = ufm;
        this.unickname = unickname;
        this.uaddress = uaddress;
        this.hgaga = hgaga;
        this.hrating = hrating;
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

    public String getHgaga() {
        return hgaga;
    }

    public void setHgaga(String hgaga) {
        this.hgaga = hgaga;
    }

    public String getHrating() {
        return hrating;
    }

    public void setHrating(String hrating) {
        this.hrating = hrating;
    }

}//Bean
