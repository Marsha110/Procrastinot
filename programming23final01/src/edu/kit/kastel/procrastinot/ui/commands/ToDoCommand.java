package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.LinkedList;
import java.util.List;

/**
 * Command to find all the not-finished tasks.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class ToDoCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "todo";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 0;

    /**
     * Instantiates a new procrastinot command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public ToDoCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        String notFinishedTaskInfo = procrastinotApp.showNotFinishedTasksInfo(findNotFinishedTasks(procrastinotApp
                .getTaskTopLevel()));
        System.out.println(notFinishedTaskInfo);
    }

    private List<Task> findNotFinishedTasks(Task startTask) {
        List<Task> notFinishedTasks = new LinkedList<>();

        if (startTask.getTaskNumber() != -1 && !startTask.isFinished()) {
            notFinishedTasks.add(startTask);
        }

        if (startTask.hasSubtask()) {
            for (Task task : startTask.getSubtask()) {
                notFinishedTasks.addAll(findNotFinishedTasks(task));
            }
        }
        return notFinishedTasks;
    }
}
