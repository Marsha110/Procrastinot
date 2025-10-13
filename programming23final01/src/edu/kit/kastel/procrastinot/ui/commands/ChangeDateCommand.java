package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Command to change the deadline of a task.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class ChangeDateCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "change-date";
    private static final String ERROR_TASK_NUMBER_FORMAT = "ERROR: Task-number shall be integer.";
    private static final String ERROR_TASK_DATE_FORMAT = "ERROR: The format of the date shall be year-month-day";
    private static final String CHANGE_DATE_SUCCEED_OUTPUT = "changed %s to %s";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;

    /**
     * Instantiates a new change-date command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public ChangeDateCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {

        //parse the task-number
        int taskNumber = parseTaskNumber(commandArguments[0]);
        if (taskNumber == -1) {
            return;
        }

        //get the corresponding task
        Task task;
        task = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
        if (task == null) {
            System.err.println(TaskNotExistsException.MESSAGE);
            return;
        }

        //parse the date
        LocalDate date;
        try {
            date = LocalDate.parse(commandArguments[1]);
        } catch (DateTimeException e) {
            System.err.println(ERROR_TASK_DATE_FORMAT);
            return;
        }

        //Set the deadline to the task.
        task.setDeadline(date);
        System.out.printf((CHANGE_DATE_SUCCEED_OUTPUT) + System.lineSeparator(),
                task.getTaskName(), commandArguments[1]);
    }

    /**
     * Parse task-number from a string.
     *
     * @param commandArguments string contains task-number
     * @return task-number of this string contains a task-number. Otherwise, return -1.
     */
    public static int parseTaskNumber(String commandArguments) {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandArguments);
        } catch (NumberFormatException e) {
            System.err.println(ERROR_TASK_NUMBER_FORMAT);
            return -1;
        }
        return taskNumber;
    }
}
