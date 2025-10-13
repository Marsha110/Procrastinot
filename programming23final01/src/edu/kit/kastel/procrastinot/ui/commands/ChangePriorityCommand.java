package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Priority;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.Command;
import edu.kit.kastel.procrastinot.ui.CommandHandler;

/**
 * Command to change the priority of a task.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class ChangePriorityCommand extends Command {
    private static final String COMMAND_NAME = "change-priority";
    private static final String ERROR_PRIORITY_NOT_EXIST = "ERROR: This priority not exists.";
    private static final String CHANGE_PRIORITY_SUCCEED_OUTPUT = "changed %s to %s";
    private static final String ARGUMENTS_LENGTH_EXCEED
            = "ERROR: The range of the length of arguments shall [1,2], but got %d";
    private static final String STRING_INIT_EMPTY = "";
    private static final int EXPECTED_ARGUMENTS_LENGTH_MAX = 2;
    private static final int EXPECTED_ARGUMENTS_LENGTH_MIN = 1;
    private final ProcrastinotApp procrastinotApp;

    /**
     * Instantiates a new change-priority command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public ChangePriorityCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler);
        this.procrastinotApp = procrastinotApp;
    }

    @Override
    public void execute(String[] commandArguments) {
        if (commandArguments.length < EXPECTED_ARGUMENTS_LENGTH_MIN || commandArguments.length > EXPECTED_ARGUMENTS_LENGTH_MAX) {
            System.err.printf((ARGUMENTS_LENGTH_EXCEED) + System.lineSeparator(), commandArguments.length);
            return;
        }

        //parse the task-number
        int taskNumber = ChangeDateCommand.parseTaskNumber(commandArguments[0]);
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

        //parse priority
        Priority priority;
        if (commandArguments.length == EXPECTED_ARGUMENTS_LENGTH_MIN) {
            task.setPriority(null);
        } else {
            priority = Priority.findPriority(commandArguments[1]);
            if (priority == null) {
                System.err.println(ERROR_PRIORITY_NOT_EXIST);
                return;
            }
            task.setPriority(priority);
        }
        System.out.printf((CHANGE_PRIORITY_SUCCEED_OUTPUT) + System.lineSeparator(),
                task.getTaskName(), task.getPriority() == null ? STRING_INIT_EMPTY : task.getPriority().toString());
    }
}
