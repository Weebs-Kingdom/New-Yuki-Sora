package de.mindcollaps.yuki.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ApiResponse {

    private JSONObject data;
    private JSONArray array;
    private boolean isArray;
    private int status;
    private String message;

    public ApiResponse(JSONObject apiResponse) {
        if(apiResponse == null)
            return;
        if(apiResponse.get("data") instanceof JSONObject){
            this.data = (JSONObject) apiResponse.get("data");
            isArray = false;
        } else {
            this.array = (JSONArray) apiResponse.get("data");
            isArray = true;
        }
        this.status = Math.toIntExact((long) apiResponse.get("status"));
        this.message = (String) apiResponse.get("msg");
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "data=" + data +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
