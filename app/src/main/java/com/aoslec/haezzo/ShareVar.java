package com.aoslec.haezzo;

public class ShareVar {

    // 공용 IP, Tomcat 주소
    public static String macIP = "192.168.134.130";
    public static String urlAddr = "http://" + macIP + ":8080/test/Haezzo/";

    // 헬퍼 최종 지원 전 임시 값 저장
    public static String hnumber = null;
    public static String hAccountImage = null;
    public static String hName = null;
    public static String hBank = null;
    public static String hAccount = null;
    public static String hIdCardImage = null;
    public static String hProfileImage = null;
    public static String hSelf = null;
    public static String hGaGa = null;
    public static String hRating = null;

    //진행중/ 진행완료 구분 값(따라 상세보기의 버튼이 달라짐)
    public static String Document_dstatus = null;

    // user, helper data
    public static String strNick = null;
    public static String strProfileImg = null;
    public static String strEmail = null;
    public static String strGender = null;
    public static String strAgeRange = null;
    public static String strAddress = null;
    public static String strUnumber = null;
}
