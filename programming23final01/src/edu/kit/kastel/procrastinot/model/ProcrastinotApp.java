package edu.kit.kastel.procrastinot.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * A Procrastinot-APP Model.
 *
 * @author uqeoc
 * @version 1.0
 */
public class ProcrastinotApp {

    private final Task taskTopLevel;
    private final Map<String, TaskList> taskLists;
    private final List<Task> trash;
    private final Set<Task> isExplored = new HashSet<>();

    /**
     * Instantiates a new Procrastinot-App.
     */
    public ProcrastinotApp() {
        taskLists = new HashMap<>();
        trash = new LinkedList<>();
        taskTopLevel = new Task();
    }

    /**
     * Add a task to this app.
     *
     * @param task task to be added.
     */
    public void addTask(Task task) {
        taskTopLevel.addASubtask(task);
    }

    /**
     * Find a task according to its taskID.
     *
     * @param task       a task in the top-level.
     * @param taskNumber the number of this task.
     * @return the task which has this taskNumber.
     */
    public Task findTask(Task task, int taskNumber) {
        if (task.getTaskNumber() == taskNumber) {
            return task;
        }

        if (task.hasSubtask()) {
            for (Task taskOne : task.getSubtask()) {
                Task taskFound = findTask(taskOne, taskNumber);
                if (taskFound != null) {
                    return taskFound;
                }
            }
        }

        return null;
    }

    /**
     * Add a new task-list to this app.
     *
     * @param taskList task-list to be added.
     * @return true if the task-list is added successful. Otherwise, return false.
     */
    public boolean addANewTaskList(TaskList taskList) {
        if (taskLists.get(taskList.getListName()) == null) {
            taskLists.put(taskList.getListName(), taskList);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find the task-list according to the name of this task-list.
     *
     * @param taskListName the name of this task-list to be found.
     * @return the task-list which has this taskListName.
     */
    public TaskList findTaskList(String taskListName) {
        return taskLists.getOrDefault(taskListName, null);
    }

    /**
     * Get a task in the trash according to its task-number.
     *
     * @param taskNumber task-number of a task.
     * @return the task if it has, otherwise, return null.
     */
    public Task getTaskInTrash(int taskNumber) {
        for (Task task : trash) {
            if (task.getTaskNumber() == taskNumber) {
                return task;
            }
            Task taskFound = findTaskInAllDirectedAndIndirectSubtasks(task, taskNumber);
            if (taskFound != null) {
                return taskFound;
            }
        }
        return null;
    }

    private Task findTaskInAllDirectedAndIndirectSubtasks(Task startTask, int taskNumberToBeFound) {
        List<Task> subtasks = startTask.getSubtask();
        for (Task task : subtasks) {
            if (task.getTaskNumber() == taskNumberToBeFound) {
                return task;
            }
            if (!task.getSubtask().isEmpty()) {
                return findTaskInAllDirectedAndIndirectSubtasks(task, taskNumberToBeFound);
            }
        }
        return null;
    }

    /**
     * Get the top-level of tasks.
     *
     * @return the top-level of tasks.
     */
    public Task getTaskTopLevel() {
        return taskTopLevel;
    }

    /**
     * Show the task-information from list of tasks and sort it and all its direct and indirect subtasks.
     * The order of these task-information will sort at first with priority.
     * If they have same parent, then sort with the add-order of these tasks.
     * If they have different parent, then sort with the task-number.
     *
     * @param tasks a list of tasks.
     * @return the filtered and sorted task-information.
     */
    public String showTasksInfo(List<Task> tasks) {
        StringJoiner tasksInfo = new StringJoiner(System.lineSeparator());
        List<Task> sortedRootTasks = getSortedRootTasks(tasks);
        tasksInfo.add(showTaskAndAllTheSubtasksInfo(sortedRootTasks, 0));
        return tasksInfo.toString();
    }

    /**
     * Filter the root-task from a list of tasks and sort it and all its direct and indirect subtasks.
     * The order of these task-information will sort at first with priority.
     * If they have same parent, then sort with the add-order of these tasks.
     * If they have different parent, then sort with the task-number.
     *
     * @param tasks a list of tasks.
     * @return a list of root-tasks.
     */
    private List<Task> getSortedRootTasks(List<Task> tasks) {
        isExplored.clear();
        List<Task> rootTasks = new LinkedList<>();
        for (Task task : tasks) {
            Task rootTask = task;
            isExplored.add(rootTask);
            while (!(rootTask.getParent() == taskTopLevel || tasks.contains(rootTask.getParent()))) {
                rootTask = rootTask.getParent();
                isExplored.add(rootTask);
            }
            if (rootTask.getParent() == taskTopLevel) {
                rootTasks.add(task);
            }
        }

        //Sort the list and all its indirect and u
        rootTasks.sort(new TaskComparator().thenComparingInt(Task::getTaskPriorityValue));
        rootTasks.sort(Comparator.comparingInt(Task::getTaskPriorityValue));
        rootTasks.forEach(Task::sortAllDirectAndIndirectSubtasks);
        return rootTasks;
    }


    /**
     * Show the information of this task and all its direct and indirect subtasks.
     *
     * @param level the level of this task in this task-tree. Level=0 means there is no backspace at the
     *              beginning of this task-information. When the level increased with 1, then two more
     *              backspace will be added at the beginning of the task-information.
     * @return a String which contains the information of itself and all its direct and indirect subtasks.
     */
    private String showTaskAndAllTheSubtasksInfo(List<Task> tasks, int level) {

        StringJoiner joinerForLine = new StringJoiner(System.lineSeparator());
        for (Task task : tasks) {
            joinerForLine.add(task.generateCurrentTaskInfo(level));
            if (task.hasSubtask()) {
                int currentLevel = level + 1;
                String subtasksInfo = showTaskAndAllTheSubtasksInfo(task.getSubtask(), currentLevel);
                if (!subtasksInfo.isEmpty()) {
                    joinerForLine.add(subtasksInfo);
                }
            }
        }
        return joinerForLine.toString();
    }

    /**
     * Show the not finished task-information from list of tasks and sort it and all its direct and indirect subtasks.
     * The order of these task-information will sort at first with priority.
     * If they have same parent, then sort with the add-order of these tasks.
     * If they have different parent, then sort with the task-number.
     *
     * @param tasks a list of tasks.
     * @return the filtered and sorted task-information.
     */
    public String showNotFinishedTasksInfo(List<Task> tasks) {
        StringJoiner tasksInfo = new StringJoiner(System.lineSeparator());
        List<Task> sortedRootTasks = getSortedRootTasks(tasks);
        tasksInfo.add(showNotFinishedTaskAndAllTheSubtasksInfo(sortedRootTasks, 0));
        return tasksInfo.toString();
    }

    /**
     * Show the not-finished task-information from list of tasks.
     *
     * @param tasks a list of tasks.
     * @param level the level of this task in this task-tree. Level=0 means there is no backspace at the
     *              beginning of this task-information. When the level increased with 1, then two more
     *              backspace will be added at the beginning of the task-information.
     * @return the filtered and sorted task-information.
     */
    public String showNotFinishedTaskAndAllTheSubtasksInfo(List<Task> tasks, int level) {
        StringJoiner joinerForLine = new StringJoiner(System.lineSeparator());
        for (Task task : tasks) {
            if (isExplored.contains(task)) {
                joinerForLine.add(task.generateCurrentTaskInfo(level));
            }
            if (task.hasSubtask()) {
                int currentLevel = level + 1;
                String subtasksInfo = showNotFinishedTaskAndAllTheSubtasksInfo(task.getSubtask(), currentLevel);
                if (!subtasksInfo.isEmpty()) {
                    joinerForLine.add(subtasksInfo);
                }
            }
        }
        return joinerForLine.toString();
    }

    /**
     * Add a task into trash.
     *
     * @param task task to be added.
     */
    public void addTaskToTrash(Task task) {
        task.getParent().removeASubtask(task);
        trash.add(task);
    }

    /**
     * Remove a task to trash.
     *
     * @param task task to be added.
     */
    public void removeTaskFromTrash(Task task) {
        trash.remove(task);
    }

    /**
     * Put all inputted tasks and subtasks.
     *
     * @param startTask a task to be converted.
     * @return a list contains
     */
    public List<Task> convertAllDirectAndIndirectSubtasksIntoAList(Task startTask) {
        List<Task> allTasks = new LinkedList<>();
        allTasks.add(startTask);
        if (startTask.hasSubtask()) {
            for (Task task : startTask.getSubtask()) {
                allTasks.addAll(convertAllDirectAndIndirectSubtasksIntoAList(task));
            }
        }
        return allTasks;
    }
}
