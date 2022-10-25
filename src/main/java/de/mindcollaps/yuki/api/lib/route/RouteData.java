package de.mindcollaps.yuki.api.lib.route;

import de.mindcollaps.yuki.api.ApiResponse;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class RouteData extends Route {

    private String databaseId;

    public void fetchData(String databaseId, YukiSora yukiSora) {
        this.databaseId = databaseId;

        if (!this.isValid())
            return;

        fetchData(yukiSora);
    }

    public <J extends RouteData> J getForeignData(Class<J> clazz, YukiSora yukiSora) {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(ForeignData.class)) {
                ForeignData anno = declaredField.getAnnotation(ForeignData.class);
                if (!anno.value().equals(clazz))
                    continue;

                String value = "";

                for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
                    if (declaredMethod.getName().equalsIgnoreCase("get" + declaredField.getName())) {
                        try {
                            value = (String) declaredMethod.invoke(this, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

                if (value == null)
                    continue;

                J data = null;
                try {
                    data = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (data == null)
                    return null;

                data.fetchData(value, yukiSora);
                return data;
            }
        }
        return null;
    }

    public void fetchData(YukiSora yukiSora) {
        if (databaseId == null)
            return;

        if (!this.isValid())
            return;

        ApiResponse response = yukiSora.getRequestHandler().get(getRoute() + "?id=" + databaseId);

        RouteParser.load(this, response.getData());
    }

    public void updateData(YukiSora yukiSora) {
        if (!this.isValid())
            return;

        ApiResponse response = yukiSora.getRequestHandler().patch(getRoute() + "?id=" + databaseId, RouteParser.pack(this));

        RouteParser.load(this, response.getData());
    }

    public void deleteData(YukiSora yukiSora) {
        if (!this.isValid())
            return;

        ApiResponse response = yukiSora.getRequestHandler().delete(getRoute() + "?id=" + databaseId, RouteParser.pack(this));
        //RouteParser.load(this, response.getData());
    }

    public void postData(YukiSora yukiSora) {
        if (!this.isValid())
            return;

        ApiResponse response = yukiSora.getRequestHandler().post(getRoute(), RouteParser.pack(this));
        if (response.getStatus() == 200)
            RouteParser.load(this, response.getData());
        else
            YukiLogger.log(new YukiLogInfo("The post request was not successful!").error());
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }
}