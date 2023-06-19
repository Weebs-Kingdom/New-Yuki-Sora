package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.YukiRoute;
import de.mindcollaps.yuki.api.lib.route.RouteField;

import java.util.Date;

@RouteClass("yukiTask")
public class YukiTask extends YukiRoute {

    @RouteField
    private String task;

    @RouteField
    private Date date;

    @RouteField
    private boolean done;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
