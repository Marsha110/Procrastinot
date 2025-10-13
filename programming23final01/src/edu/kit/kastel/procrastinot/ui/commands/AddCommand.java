package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Priority;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.Command;
import edu.kit.kastel.procrastinot.ui.CommandHandler;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Command to add a task.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class AddCommand extends Command {
    private static final int MAX_ARGUMENTS_LENGTH = 3;
    private static final String ARGUMENTS_LENGTH_EXCEED
            = "ERROR: The range of the length of arguments shall [1,3], but got %d";
    private static final String COMMAND_NAME = "add";
    private static final String ERROR_DATE_FORMAT = "ERROR: The format of date is wrong";
    private static final String ERROR_SECOND_PARAMETER = "ERROR: The second parameter shall be a priority or a date";
    private static final String ERROR_PRIORITY_FORMAT = "ERROR: Priority shall be HI, MD or LO.";
    private static final String ADD_SUCCEED_OUTPUT = "added %d: %s";
    private static final int COMMAND_HAS_ONE_ARGUMENT = 1;
    private static final int COMMAND_HAS_TWO_ARGUMENTS = 2;
    private static final int COMMAND_HAS_THREE_ARGUMENTS = 3;
    private int taskNumber = 0;
    private final ProcrastinotApp procrastinotApp;

    /**
     * Instantiates a new add command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public AddCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler);
        this.procrastinotApp = procrastinotApp;
    }

    @Override
    public void execute(String[] commandArguments) {
        if (commandArguments.length < 1 || commandArguments.length > MAX_ARGUMENTS_LENGTH) {
            System.err.printf((ARGUMENTS_LENGTH_EXCEED) + System.lineSeparator(), commandArguments.length);
            return;
        }

        Priority priority = null;
        LocalDate taskDeadline = null;
        boolean dateHasCorrectFormat = true;
        String taskName = commandArguments[0];

        if (commandArguments.length == COMMAND_HAS_THREE_ARGUMENTS) {
            priority = Priority.findPriority(commandArguments[1]);
            if (priority == null) {
                System.err.println(ERROR_PRIORITY_FORMAT);
                return;
            }
            //parse the third parameter
            try {
                taskDeadline = LocalDate.parse(commandArguments[2]);
            } catch (DateTimeException e) {
                System.err.println(ERROR_DATE_FORMAT);
                return;
            }
        } else if (commandArguments.length == COMMAND_HAS_TWO_ARGUMENTS) {
            //parse the second parameter

            //verify if the second parameter is a priority
            priority = Priority.findPriority(commandArguments[1]);

            //verify if the second parameter is a valid date
            if (priority == null) {
                try {
                    taskDeadline = LocalDate.parse(commandArguments[1]);
                } catch (DateTimeException e) {
                    dateHasCorrectFormat = false;
                }
            }
            if (priority == null && !dateHasCorrectFormat) {
                System.err.println(ERROR_SECOND_PARAMETER);
                return;
            }
        }

        // add a task according to different number of arguments.
        switch (commandArguments.length) {
            case COMMAND_HAS_ONE_ARGUMENT -> procrastinotApp.addTask(new Task(++taskNumber, taskName, null,
                    null, procrastinotApp.getTaskTopLevel()));
            case COMMAND_HAS_TWO_ARGUMENTS -> addNewTask(taskName, priority, taskDeadline);
            default -> procrastinotApp
                    .addTask(new Task(++taskNumber, taskName, priority, taskDeadline,
                            procrastinotApp.getTaskTopLevel()));
        }
        System.out.printf((ADD_SUCCEED_OUTPUT) + System.lineSeparator(), taskNumber, taskName);
    }

    private void addNewTask(String taskName, Priority priority, LocalDate taskDeadline) {
        if (priority != null) {
            procrastinotApp.addTask(new Task(++taskNumber, taskName, priority,
                    null, procrastinotApp.getTaskTopLevel()));
        } else if (taskDeadline != null) {
            procrastinotApp.addTask(new Task(++taskNumber, taskName, null,
                    taskDeadline, procrastinotApp.getTaskTopLevel()));
        }
    }
}
