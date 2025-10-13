package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

/**
 * Command to find duplicated tasks.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class DuplicatesCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "duplicates";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;
    private static final String NO_DUPLICATES = "Found 0 duplicates: ";
    private static final String DUPLICATES_OUTPUT = "Found %d duplicates: %s";
    private static final String TASK_NUMBER_SEPARATOR = ", ";

    /**
     * Instantiates a new duplicates command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public DuplicatesCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        String duplicateInfo = findDuplicatedTasks();
        if (duplicateInfo != null && !duplicateInfo.isEmpty()) {
            System.out.println(duplicateInfo);
        } else {
            System.out.println(NO_DUPLICATES);
        }
    }

    /**
     * Find duplicated tasks and convert the task-information to string-format.
     *
     * @return the task-information of all the duplicated tasks.
     */
    public String findDuplicatedTasks() {
        List<Task> allDuplicate = new ArrayList<>();
        List<Task> allTasks
                = procrastinotApp.convertAllDirectAndIndirectSubtasksIntoAList(procrastinotApp.getTaskTopLevel());
        //check every two tasks if they are duplicated.
        for (int taskOneIndex = 0; taskOneIndex < allTasks.size(); taskOneIndex++) {
            for (int taskTwoIndex = taskOneIndex + 1; taskTwoIndex < allTasks.size(); taskTwoIndex++) {
                Task taskOne = allTasks.get(taskOneIndex);
                Task taskTwo = allTasks.get(taskTwoIndex);
                if (isDuplicate(taskOne, taskTwo)) {
                    if (!allDuplicate.contains(taskOne)) {
                        allDuplicate.add(taskOne);
                    }
                    if (!allDuplicate.contains(taskTwo)) {
                        allDuplicate.add(taskTwo);
                    }
                }
            }
        }
        // sort for every subList
        allDuplicate.sort(Comparator.comparingInt(Task::getTaskNumber));
        return listToString(allDuplicate);
    }

    private String listToString(List<Task> tasks) {
        StringJoiner tasksNumberJoiner = new StringJoiner(TASK_NUMBER_SEPARATOR);
        tasks.forEach(task -> tasksNumberJoiner.add(Integer.toString(task.getTaskNumber())));
        return String.format(DUPLICATES_OUTPUT, tasks.size(), tasksNumberJoiner);
    }

    private boolean isDuplicate(Task taskOne, Task taskTwo) {
        if (!taskOne.getTaskName().equals(taskTwo.getTaskName())) {
            return false;
        } else {
            return taskOne.getDeadline() == null || taskTwo.getDeadline() == null
                    || taskOne.getDeadline().isEqual(taskTwo.getDeadline());
        }
    }
}
