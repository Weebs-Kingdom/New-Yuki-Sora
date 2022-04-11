package yuki.api.lib;

import yuki.core.YukiProperties;

public abstract class Route {

    public String getRoute(){
        if(!this.getClass().isAnnotationPresent(RouteClass.class))
            return null;

        return YukiProperties.getProperties(YukiProperties.dPDbApiUrl) + "/" + this.getClass().getAnnotation(RouteClass.class).value();
    }

    public boolean isValid(){
        if(!this.getClass().isAnnotationPresent(RouteClass.class))
            return false;
        return true;
    }
}
