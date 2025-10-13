package edu.kit.kastel.procrastinot.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * A task Model.
 *
 * @author uqeoc
 * @version 1.0
 */
public class Task {
    private static final String TASK_STATUS = "- [%s]";
    private static final String TASK_PRIORITY = "[%s]";
    private static final String TASK_TAG = "(%s)";
    private static final String TASK_DEADLINE = "--> %s";
    private static final String TASK_FINISHED_SIGNAL = "x";
    private static final String WHITE_SPACE = " ";
    private static final String STRING_EMPTY = "";
    private static final String COLON = ":";
    private static final String COMMA = ", ";
    private static final int NULL_PRIORITY_VALUE = 5;
    private final int taskNumber;
    private final String name;
    private Priority priority;
    private LocalDate deadline;
    private boolean isFinished;
    private final List<Task> subtasks;
    private Task parent;
    private final Set<String> tags;
    private final Set<TaskList> taskLists;
    private boolean isDeleted;

    /**
     * Instantiates a new task.
     *
     * @param taskNumber the number of this task.
     * @param name       the name of this task.
     * @param priority   the priority of this task.
     * @param deadline   the deadline of this task.
     * @param parent     the parent of this task.
     */
    public Task(int taskNumber, String name, Priority priority, LocalDate deadline, Task parent) {
        this.name = name;
        isFinished = false;
        this.taskNumber = taskNumber;
        subtasks = new LinkedList<>();
        this.parent = parent;
        taskLists = new HashSet<>();
        tags = new HashSet<>();
        this.priority = priority;
        this.deadline = deadline;
        this.isDeleted = false;
    }

    /**
     * Instantiates a new task.
     */
    public Task() {
        this.taskNumber = -1;
        this.name = STRING_EMPTY;
        subtasks = new LinkedList<>();
        tags = new HashSet<>();
        taskLists = new HashSet<>();
        this.parent = this;
        this.isDeleted = false;
    }

    /**
     * Set the priority of this task.
     *
     * @param priority new priority.
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Get the priority of this task.
     *
     * @return the priority of this task.
     */
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * Set the deadline of this task.
     *
     * @param deadline new deadline.
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = Objects.requireNonNull(deadline);
    }

    /**
     * Get the task-number of this task.
     *
     * @return the number of this task.
     */
    public int getTaskNumber() {
        return this.taskNumber;
    }

    /**
     * Get the task-name.
     *
     * @return the name of this task.
     */
    public String getTaskName() {
        return this.name;
    }

    /**
     * Get the priority-value.
     *
     * @return the value of the priority.
     */
    public int getTaskPriorityValue() {
        if (priority == null) {
            return NULL_PRIORITY_VALUE;
        }
        return priority.ordinal();
    }

    /**
     * Set the status of this task.
     *
     * @param status the status(finished or not finished) of this task.
     */
    public void setTaskStatus(boolean status) {
        this.isFinished = status;
    }


    /**
     * Add a new subtask to this task.
     *
     * @param subtask a new subtask.
     */
    public void addASubtask(Task subtask) {
        if (subtask.getParent() != null) {
            subtask.getParent().removeASubtask(subtask);
        }
        this.subtasks.add(subtask);
        subtask.setParent(this);
    }

    /**
     * Set the tag of this task.
     *
     * @param tag tag of this task.
     * @return true if this new tag is different with the old tag.Otherwise, return false.
     */
    public boolean addTag(String tag) {
        if (!tags.contains(tag)) {
            this.tags.add(tag);
            return true;
        }
        return false;
    }

    /**
     * Get the tag of this task.
     *
     * @return the tag of this task.
     */
    public Set<String> getTags() {
        return Set.copyOf(tags);
    }

    /**
     * Generate the information of this task.
     *
     * @param level the level of this task in this task-tree. Level=0 means there is no backspace at the
     *              beginning of this task-information. When the level increased with 1, then two more
     *              backspace will be added at the beginning of the task-information.
     * @return a String which contains the information of this task.
     */
    public String generateCurrentTaskInfo(int level) {
        StringJoiner taskInfo = new StringJoiner(WHITE_SPACE);

        // generate the blank at the beginning for different level.
        int taskInfoOutputStartPosition = level;
        while (taskInfoOutputStartPosition != 0) {
            taskInfo.add(WHITE_SPACE);
            taskInfoOutputStartPosition--;
        }
        //generate the current task-info
        String taskStatusInfo = String.format(TASK_STATUS, isFinished ? TASK_FINISHED_SIGNAL : WHITE_SPACE);
        taskInfo.add(taskStatusInfo);
        String taskPriorityInfo;
        if (priority == null) {
            if (!tags.isEmpty() || deadline != null) {
                taskInfo.add(name + COLON);
            } else {
                taskInfo.add(name);
            }
        } else {
            taskInfo.add(name);
            taskPriorityInfo = String.format(TASK_PRIORITY, priority.name());
            if (tags.isEmpty() && deadline == null) {
                taskInfo.add(taskPriorityInfo);
            } else {
                taskInfo.add(taskPriorityInfo + COLON);
            }
        }
        if (!tags.isEmpty()) {
            StringJoiner allTags = new StringJoiner(COMMA);
            tags.forEach(allTags::add);
            taskInfo.add(String.format(TASK_TAG, allTags));
        }
        if (deadline != null) {
            taskInfo.add(String.format(TASK_DEADLINE, deadline));
        }
        return taskInfo.toString();
    }

    /**
     * Get the parent task.
     *
     * @return the parent task.
     */
    public Task getParent() {
        return parent;
    }

    /**
     * Get the deadline of this task.
     *
     * @return the deadline of this task.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Set the list which the task belongs.
     *
     * @param taskList the list which the task belongs.
     */
    public void assignTaskToATaskList(TaskList taskList) {
        this.taskLists.add(taskList);
    }

    /**
     * Remove a subtask.
     *
     * @param task to be removed subtask.
     */
    public void removeASubtask(Task task) {
        subtasks.remove(task);
    }

    /**
     * Remove the task from its list and its parent-task.
     */
    public void removeTaskAndAllDirectAndIndirectSubtasksFromAllItsTaskLists() {
        isDeleted = true;
        taskLists.forEach(taskList -> {
            if (taskList != null) {
                taskList.removeTask(this);
            }
        });
        if (!subtasks.isEmpty()) {
            for (Task task : subtasks) {
                task.removeTaskAndAllDirectAndIndirectSubtasksFromAllItsTaskLists();
            }
        }
        if (!parent.isDeleted()) {
            parent.removeASubtask(this);
        }
    }

    /**
     * Get if the task is finished.
     *
     * @return true if the task is finished. Otherwise, return false.
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Check if it has a specified sub-task.
     *
     * @return a list which contains all subtasks.
     */
    public List<Task> getSubtask() {
        return List.copyOf(subtasks);
    }

    /**
     * Set the parent-task.
     *
     * @param parentTask parent-task
     */
    public void setParent(Task parentTask) {
        parent = parentTask;
    }

    /**
     * Get lists which contains this task.
     *
     * @return alle the list, which contains this task.
     */
    public Set<TaskList> getTaskLists() {
        return Set.copyOf(taskLists);
    }

    /**
     * If the task has subtasks.
     *
     * @return true if it has. Otherwise, return false.
     */
    public boolean hasSubtask() {
        return !subtasks.isEmpty();
    }

    /**
     * Sort all direct and indirect subtasks.
     */
    public void sortAllDirectAndIndirectSubtasks() {
        subtasks.sort(new TaskComparator());
        subtasks.sort(Comparator.comparingInt(Task::getTaskPriorityValue));
        for (Task task : subtasks) {
            if (task.hasSubtask()) {
                task.sortAllDirectAndIndirectSubtasks();
            }
        }
    }

    /**
     * Check if this task has been deleted.
     *
     * @return true if it has been deleted. Otherwise, return false.
     */
    public boolean isDeleted() {
        return isDeleted;
    }
}
