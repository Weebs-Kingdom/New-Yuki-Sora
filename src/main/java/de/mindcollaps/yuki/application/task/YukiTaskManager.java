package de.mindcollaps.yuki.application.task;

import de.mindcollaps.yuki.api.lib.data.YukiTask;
import de.mindcollaps.yuki.application.task.tasks.Task;
import de.mindcollaps.yuki.application.task.tasks.TaskGiveRole;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

@YukiLogModule(name = "Yuki Task Manager")
public class YukiTaskManager {

    boolean threadRunning = false;
    private Timer timer;
    private final YukiSora yukiSora;

    public YukiTaskManager(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    public void checkAndUpdate() {
        YukiTask[] tasks = new YukiTask().fetchAll(YukiTask.class, yukiSora);
        for (YukiTask task : tasks) {
            if (task.isDone()) {
                task.deleteData(yukiSora);
            } else {
                Task runTask = null;
                try {
                    runTask = parseTask(task);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("The Task parser had an error parsing the task!").trace(e));
                    return;
                }

                if (runTask != null)
                    runTask.action(yukiSora);

                task.setDone(true);
                task.updateData(yukiSora);
            }
        }
    }

    public Task parseTask(YukiTask t) throws Exception {
        JSONObject object = FileUtils.convertStringToJson(t.getTask());

        String isnt = (String) object.get("task");
        JSONArray array = (JSONArray) object.get("data");

        if (isnt.toLowerCase().equals("giverole")) {
            try {
                return constructTask(array, TaskGiveRole.class);
            } catch (Exception e) {
                YukiLogger.log(new YukiLogInfo("The Task parser couldn't load the data").trace(e));
                return null;
            }
        }

        YukiLogger.log(new YukiLogInfo("The Task parser didn't found a matching instruction!").trace(null));
        return null;
    }

    public <T extends Task> T constructTask(JSONArray data, Class<T> clazz) throws Exception {
        T task = clazz.getConstructor().newInstance();

        for (Field declaredField : clazz.getDeclaredFields()) {
            for (Object datum : data) {
                JSONObject arrayData = (JSONObject) datum;

                if (arrayData.containsKey(declaredField.getName())) {
                    for (Method declaredMethod : clazz.getDeclaredMethods()) {
                        if (declaredMethod.getName().equalsIgnoreCase("set" + declaredField.getName())) {
                            declaredMethod.invoke(task, arrayData.get(declaredField.getName()));
                        }
                    }
                }
            }
        }

        return task;
    }

    public void startUpdateThread() {
        if (threadRunning) {
            YukiLogger.log(new YukiLogInfo("Trying to start the thread, but its already running!").warning());
        } else {
            threadRunning = true;
            if (this.timer != null)
                this.timer.purge();
            else
                this.timer = new Timer();
            YukiLogger.log(new YukiLogInfo("Starting Yuki Task Manager ..."));
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    try {
                        checkAndUpdate();
                    } catch (Exception e){
                        YukiLogger.log(new YukiLogInfo("Running a task threw an error").trace(e));
                    }
                }
            };

            timer.schedule(tt, 10 * 10 * 60, 10 * 10 * 60 * 2);
        }
    }
}
