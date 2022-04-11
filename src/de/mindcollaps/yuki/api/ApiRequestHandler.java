package yuki.api;

import org.json.simple.JSONObject;
import yuki.core.YukiProperties;
import yuki.core.YukiSora;
import yuki.util.FileUtils;

public class ApiRequestHandler {

    private YukiSora yukiSora;

    public ApiRequestHandler(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    public ApiResponse get(String route){
        String responseMsg = yukiSora.getNetworkManager().get(route, token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    public ApiResponse post(String route, JSONObject json){
        String responseMsg = yukiSora.getNetworkManager().post(route, json.toJSONString(), token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    public ApiResponse patch(String route, JSONObject json){
        String responseMsg = yukiSora.getNetworkManager().patch(route, json.toJSONString(), token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    public ApiResponse delete(String route, JSONObject json){
        String responseMsg = yukiSora.getNetworkManager().delete(route, json.toJSONString(), token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    private String token(){
        return YukiProperties.getProperties(YukiProperties.dPDbApiToken);
    }
}
