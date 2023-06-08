package de.mindcollaps.yuki.application.task.tasks;

import de.mindcollaps.yuki.core.YukiSora;

public abstract class Task {

    private final String instruction;

    public abstract boolean action(YukiSora yukiSora);

    public Task(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}
