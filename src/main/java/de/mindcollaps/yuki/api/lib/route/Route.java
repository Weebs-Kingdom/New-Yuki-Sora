package de.mindcollaps.yuki.api.lib.route;

import de.mindcollaps.yuki.core.YukiProperties;

public abstract class Route {

    public String getRoute(){
        return getRoute(this.getClass());
    }

    public static <T extends Route> String getRoute(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(RouteClass.class))
            return null;

        return YukiProperties.getProperties(YukiProperties.dPDbApiUrl) + "/" + ((RouteClass) clazz.getAnnotation(RouteClass.class)).value();
    }

    public boolean isValid() {
        return this.getClass().isAnnotationPresent(RouteClass.class);
    }
}
