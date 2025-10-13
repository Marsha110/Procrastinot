package edu.kit.kastel.procrastinot.model;

import java.util.Comparator;

/**
 *  Create a comparator for task, to sort the task and all its direct and indirect subtasks with following rules:
 *  If they have same parent, then sort with the add-order of these tasks.
 *  If they have different parent, then sort with the task-number.
 *
 * @author uqeoc
 * @version 1.0
 */
public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task taskOne, Task taskTwo) {
        if (taskOne.getParent() != null && taskTwo.getParent() != null
                && taskOne.getParent() == taskTwo.getParent()) {
            int taskOneIndex = taskOne.getParent().getSubtask().indexOf(taskOne);
            int taskTwoIndex = taskTwo.getParent().getSubtask().indexOf(taskTwo);
            return taskOneIndex - taskTwoIndex;
        } else if (taskOne.getParent() == null && taskTwo.getParent() == null) {
            int taskOneIndex = taskOne.getParent().getSubtask().indexOf(taskOne);
            int taskTwoIndex = taskOne.getParent().getSubtask().indexOf(taskTwo);
            return taskOneIndex - taskTwoIndex;
        } else {
            int taskOneNumber = taskOne.getTaskNumber();
            int taskTwoIndex = taskTwo.getTaskNumber();
            return taskOneNumber - taskTwoIndex;
        }
    }
}
