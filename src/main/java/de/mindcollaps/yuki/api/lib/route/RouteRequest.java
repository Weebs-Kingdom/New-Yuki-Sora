package de.mindcollaps.yuki.api.lib.route;

import de.mindcollaps.yuki.api.ApiResponse;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;

public abstract class RouteRequest<T extends YukiRoute> extends Route {

    private final Class<T> clazz;

    public RouteRequest(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T makeRequestSingle(YukiSora yukiSora) {
        T[] data = makeRequest(yukiSora);

        if (data == null)
            return null;
        if (data.length == 0)
            return null;
        return data[0];
    }

    public T[] makeRequest(YukiSora yukiSora) {
        T[] erData = (T[]) Array.newInstance(clazz, 0);
        ApiResponse response = null;

        try {
            response = yukiSora.getRequestHandler().post(getRoute(), RouteParser.pack(this));
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("The request " + clazz.getSimpleName() + " had an error while fetching the data from the API!").trace(e));
        }

        if (response == null) {
            return erData;
        }

        if (response.getStatus() != 200)
            return erData;

        T[] ar = null;

        if (response.isArray()) {
            ar = (T[]) Array.newInstance(clazz, response.getArray().size());
            for (int i = 0; i < response.getArray().size(); i++) {
                JSONObject d = (JSONObject) response.getArray().get(i);
                T t = null;
                try {
                    t = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                }

                if (t == null)
                    continue;

                RouteParser.load(t, d);
                ar[i] = t;
            }
        } else {
            ar = (T[]) Array.newInstance(clazz, 1);
            T t = null;
            try {
                t = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
            }

            if (t == null)
                return erData;

            RouteParser.load(t, response.getData());
            ar[0] = t;
        }

        return ar;
    }
}
