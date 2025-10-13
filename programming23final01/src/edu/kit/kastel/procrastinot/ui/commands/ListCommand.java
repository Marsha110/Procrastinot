package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.TaskList;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

/**
 * Command to show information of all directed and indirect tasks.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class ListCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "list";
    private static final String ERROR_LIST_NOT_EXIST = "ERROR: The list doesn't exist.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new list command.
     *
     * @param commandHandler the command handler
     * @param procrastinotApp        a procrastinot-APP
     */
    public ListCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        TaskList taskList = procrastinotApp.findTaskList(commandArguments[0]);
        if (taskList == null) {
            System.err.println(ERROR_LIST_NOT_EXIST);
        } else {
            System.out.println(taskList.showAllTasksInfo(procrastinotApp));
        }
    }
}
