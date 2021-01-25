package com.example.listmovie.utils;

import java.util.HashMap;
import java.util.Map;

import static com.example.listmovie.data.Api.Token;

public class Utils {

    /**
     * ErrorMessage
     * @param httpErrorCode int
     * @param Rule String
     * @return String
     */
    public static String ErrorMessage(int httpErrorCode, String Rule){
        String errorMessage = "Unknown Error";
        if (httpErrorCode == 404) {
            errorMessage = "404 : Resource not found.";
        } else if (httpErrorCode == 401) {
            errorMessage = "401 : Please re-login again.";
        } else if (httpErrorCode == 400) {
            if (Rule.equals("GET")) {
                errorMessage = "400 : Bad Request !";
            }else {
                errorMessage = "400 : Check your input !";
            }
        } else if (httpErrorCode == 500) {
            errorMessage = "500 : Something is getting wrong.";
        }else if (httpErrorCode == 403){
            errorMessage = "403 : Season end, Please re-login again.";
        }

        return errorMessage;
    }

    public static Map<String, String> getToken(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + Token);
        return headers;
    }

}
