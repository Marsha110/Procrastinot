package edu.kit.kastel.procrastinot.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A task-list model.
 *
 * @author uqeoc
 * @version 1.0
 */
public class TaskList {
    private final String listName;
    private final List<Task> tasks;
    private String tag;
    private final Set<String> inputtedTag;

    /**
     * Instantiates a new task-list.
     *
     * @param listName the name of this list
     */
    public TaskList(String listName) {
        this.listName = listName;
        tasks = new LinkedList<>();
        inputtedTag = new HashSet<>();
    }

    /**
     * Get the name of this list.
     *
     * @return the name of this list.
     */
    public String getListName() {
        return this.listName;
    }

    /**
     * Add a task to this list.
     *
     * @param task task to be added.
     */
    public void addTaskToList(Task task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
            task.assignTaskToATaskList(this);
            List<Task> subtasks = task.getSubtask();
            if (!subtasks.isEmpty()) {
                for (Task subtask : subtasks) {
                    addTaskToList(subtask);
                }
            }
        }
    }

    /**
     * Check if a list contains a specified task.
     *
     * @param task to be checked task.
     * @return true if list contains task. Otherwise, return false.
     */
    public boolean contain(Task task) {
        return tasks.contains(task);
    }

    /**
     * Show all the direct and indirect task-information in this list.
     *
     * @param procrastinotApp the app which this list belongs.
     * @return All the direct and indirect task-information in this list.
     */
    public String showAllTasksInfo(ProcrastinotApp procrastinotApp) {
        return procrastinotApp.showTasksInfo(tasks);
    }

    /**
     * Get if the list is empty.
     *
     * @return true if the list is empty. Otherwise, return false.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Remove a task from the list.
     *
     * @param task the task to be removed.
     */
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    /**
     * Set the tag of this list.
     *
     * @param tag new tag of this list.
     * @return true if the tag is not duplicated. Otherwise, return false.
     */
    public boolean setTag(String tag) {
        if (!inputtedTag.contains(tag)) {
            this.tag = tag;
            this.inputtedTag.add(tag);
            return true;
        }
        return false;
    }

    /**
     * Get the tag of this list.
     *
     * @return the tag of this list.
     */
    public String getTag() {
        return this.tag;
    }
}
