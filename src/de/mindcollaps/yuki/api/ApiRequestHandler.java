package de.mindcollaps.yuki.api;

import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.FileUtils;
import org.json.simple.JSONObject;

public class ApiRequestHandler {

    private final YukiSora yukiSora;

    public ApiRequestHandler(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    public ApiResponse get(String route) {
        String responseMsg = yukiSora.getNetworkManager().get(route, token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    public ApiResponse post(String route, JSONObject json) {
        String responseMsg = yukiSora.getNetworkManager().post(route, json.toJSONString(), token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    public ApiResponse patch(String route, JSONObject json) {
        String responseMsg = yukiSora.getNetworkManager().patch(route, json.toJSONString(), token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    public ApiResponse delete(String route, JSONObject json) {
        String responseMsg = yukiSora.getNetworkManager().delete(route, json.toJSONString(), token());

        return new ApiResponse(FileUtils.convertStringToJson(responseMsg));
    }

    private String token() {
        return YukiProperties.getProperties(YukiProperties.dPDbApiToken);
    }
}
