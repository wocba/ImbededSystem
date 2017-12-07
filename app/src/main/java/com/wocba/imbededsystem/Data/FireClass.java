package com.wocba.imbededsystem.Data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinwo on 2017-12-05.
 */

public class FireClass {
    public String firebaseKey; // Firebase Realtime Database 에 등록된 Key 값
    public String userEmail; // 사용자 이메일
    public String PhotoUrl; // 사진 URL
    public String lati; // 위도
    public String longi; // 경도
    public String comment; // 내용

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userEmail", userEmail);
        result.put("PhotoUrl", PhotoUrl);
        result.put("lati", lati);
        result.put("longi", longi);
        result.put("comment", comment);
        result.put("firebaseKey", firebaseKey);

        return result;
    }
}
