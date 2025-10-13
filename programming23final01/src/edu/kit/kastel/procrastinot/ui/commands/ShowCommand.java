package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.LinkedList;
import java.util.List;

/**
 * Command to show the information of a given task and all its direct and indirect subtasks.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class ShowCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "show";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new show command.
     *
     * @param commandHandler the command handler
     * @param procrastinotApp        a procrastinot-APP
     */
    public ShowCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        //parse the task-number
        int taskNumber = ChangeDateCommand.parseTaskNumber(commandArguments[0]);
        if (taskNumber == -1) {
            return;
        }

        Task task;
        task = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
        if (task == null) {
            System.err.println(TaskNotExistsException.MESSAGE);
            return;
        }
        List<Task> tasks = new LinkedList<>();
        tasks.add(task);
        System.out.println(procrastinotApp.showTasksInfo(tasks));
    }
}
