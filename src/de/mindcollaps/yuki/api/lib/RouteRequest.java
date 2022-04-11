package yuki.api.lib;

import org.json.simple.JSONObject;
import yuki.api.ApiResponse;
import yuki.console.log.YukiLogInfo;
import yuki.console.log.YukiLogger;
import yuki.core.YukiSora;

public abstract class RouteRequest<T extends RouteData> extends Route {

    private Class<T> clazz;

    public RouteRequest(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T makeRequestSingle(YukiSora yukiSora){
        T[] data = makeRequest(yukiSora);

        if(data == null)
            return null;
        if(data.length == 0)
            return null;
        return data[0];
    }

    public T[] makeRequest(YukiSora yukiSora) {
        RouteData[] erData = new RouteData[0];
        ApiResponse response = null;

        try {
            response = yukiSora.getRequestHandler().post(getRoute(), RouteParser.pack(this));
        } catch (Exception e){
            YukiLogger.log(new YukiLogInfo("The request " + clazz.getSimpleName() + " had an error while fetching the data from the API!").trace(e));
        }

        if(response == null){
            return (T[]) erData;
        }

        RouteData[] ar = null;

        if (response.isArray()) {
            ar = new RouteData[response.getArray().size()];
            for (int i = 0; i < response.getArray().size(); i++) {
                JSONObject d = (JSONObject) response.getArray().get(i);
                T t = null;
                try {
                    t = clazz.newInstance();
                } catch (Exception e) {
                }

                if (t == null)
                    continue;

                RouteParser.load(t, d);
            }
        } else {
            ar = new RouteData[1];
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (Exception e) {
            }

            if (t == null)
                return (T[]) erData;

            RouteParser.load(t, response.getData());
            ar[0] = t;
        }

        return (T[]) ar;
    }
}
