package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.*;

@RouteClass("user-job")
public class UserJob extends YukiRoute {

    @RouteField
    private int jobXP;

    @RouteField
    private int jobLevel;

    @RouteField
    private String jobPosition;

    @RouteField
    private int jobStreak;

    @RouteField
    @ForeignData(Job.class)
    private DatabaseId job;

    public int getJobXP() {
        return jobXP;
    }

    public void setJobXP(int jobXP) {
        this.jobXP = jobXP;
    }

    public int getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(int jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public int getJobStreak() {
        return jobStreak;
    }

    public void setJobStreak(int jobStreak) {
        this.jobStreak = jobStreak;
    }

    public DatabaseId getJob() {
        return job;
    }

    public void setJob(DatabaseId job) {
        this.job = job;
    }
}
