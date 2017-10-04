package com.lockit;

import java.util.List;

public class ApplicationGroup {
    private int priority;
    private String name;
    private List<Application> applications;

    public ApplicationGroup(int priority, String name, List<Application> applications) {
        this.priority = priority;
        this.name = name;
        this.applications = applications;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
