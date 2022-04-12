package de.mindcollaps.yuki.api.lib;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RouteParser {

    public static JSONObject pack(Object obj) {
        if (obj.getClass().isAnnotationPresent(RouteClass.class)) {
            JSONObject jsonObject = new JSONObject();
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(RouteField.class))
                    continue;
                RouteField routeField = field.getAnnotation(RouteField.class);
                String fieldName = routeField.dataName();
                if (fieldName.length() == 0)
                    fieldName = field.getName();

                Object data = null;
                try {
                    for (Method declaredMethod : obj.getClass().getDeclaredMethods()) {
                        if (declaredMethod.getName().equalsIgnoreCase("get" + fieldName)) {
                            data = declaredMethod.invoke(obj, null);
                            break;
                        }
                    }
                } catch (Exception e) {
                }

                if (data instanceof String[]) {
                    String[] dat = (String[]) data;
                    data = new JSONArray();
                    for (String s : dat) {
                        ((JSONArray) data).add(s);
                    }
                }

                if (data instanceof int[]) {
                    int[] dat = (int[]) data;
                    data = new JSONArray();
                    for (int s : dat) {
                        ((JSONArray) data).add(s);
                    }
                }

                jsonObject.put(fieldName, data);
            }

            return jsonObject;
        } else return null;
    }

    public static void load(Object obj, JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        if (obj.getClass().isAnnotationPresent(RouteClass.class)) {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(RouteField.class))
                    continue;
                RouteField routeField = field.getAnnotation(RouteField.class);
                String fieldName = routeField.dataName();
                if (fieldName.length() == 0)
                    fieldName = field.getName();

                if (!jsonObject.containsKey(fieldName))
                    continue;

                Object data = jsonObject.get(fieldName);

                if (data == null)
                    continue;

                if (data instanceof JSONArray) {
                    JSONArray array = (JSONArray) data;
                    data = array.toArray(new String[array.size()]);
                }

                if(data instanceof Long){
                    long d = (long) data;
                    data = Math.toIntExact(d);
                }

                for (Method method : obj.getClass().getDeclaredMethods()) {
                    if (method.getName().equalsIgnoreCase("set" + fieldName)) {
                        try {
                            method.invoke(obj, data);
                        } catch (Exception e) {
                            YukiLogger.log(new YukiLogInfo("The packer found a field without a setter method or the datatype does not match! Data class: "  + data.getClass() + " method parameter: " + method.getParameterTypes()[0]).trace(e));
                        }
                        break;
                    }
                }
            }
        }
    }
}
