package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.TaskList;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

/**
 * Command to add a list to the app.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class AddListCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "add-list";
    private static final String ADD_TASK_LIST_SUCCEED_OUTPUT = "added %s";
    private static final String LIST_NAME_REGEX = "^[A-Za-z]+$";
    private static final String ERROR_LIST_NAME_FORMAT = "ERROR: The list-name can only contain A-Z or a-z.";
    private static final String ERROR_LIST_NAME_DUPLICATED = "ERROR: List-name is duplicated.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new add-list command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-App.
     */
    public AddListCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        String listName;
        if (commandArguments[0].matches(LIST_NAME_REGEX)) {
            listName = commandArguments[0];
        } else {
            System.err.println(ERROR_LIST_NAME_FORMAT);
            return;
        }
        if (procrastinotApp.addANewTaskList(new TaskList(listName))) {
            System.out.printf(((ADD_TASK_LIST_SUCCEED_OUTPUT) + System.lineSeparator()), listName);
        } else {
            System.err.println(ERROR_LIST_NAME_DUPLICATED);
        }
    }
}
