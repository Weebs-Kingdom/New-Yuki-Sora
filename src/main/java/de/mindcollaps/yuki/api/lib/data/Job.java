package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.YukiRoute;
import de.mindcollaps.yuki.api.lib.route.RouteField;

@RouteClass("job")
public class Job extends YukiRoute {

    @RouteField
    private int earningTrainee;

    @RouteField
    private int earningCoworker;

    @RouteField
    private int earningHeadOfDepartment;

    @RouteField
    private int earningManager;

    @RouteField
    private String jobName;

    @RouteField
    private String shortName;

    @RouteField
    private String doing;

    public int getEarningTrainee() {
        return earningTrainee;
    }

    public void setEarningTrainee(int earningTrainee) {
        this.earningTrainee = earningTrainee;
    }

    public int getEarningCoworker() {
        return earningCoworker;
    }

    public void setEarningCoworker(int earningCoworker) {
        this.earningCoworker = earningCoworker;
    }

    public int getEarningHeadOfDepartment() {
        return earningHeadOfDepartment;
    }

    public void setEarningHeadOfDepartment(int earningHeadOfDepartment) {
        this.earningHeadOfDepartment = earningHeadOfDepartment;
    }

    public int getEarningManager() {
        return earningManager;
    }

    public void setEarningManager(int earningManager) {
        this.earningManager = earningManager;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDoing() {
        return doing;
    }

    public void setDoing(String doing) {
        this.doing = doing;
    }
}
