package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.LinkedList;
import java.util.List;

import static edu.kit.kastel.procrastinot.ui.commands.RestoreCommand.getNumberOfDirectedAndIndirectSubtasks;

/**
 * Command to mark a task and all its subtasks with the inverse finish-status of this task.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class ToggleCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "toggle";
    private static final String TOGGLE_SUCCEED_OUTPUT = "toggled %s and %d subtasks";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new toggle command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public ToggleCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        //parse the task-number
        int taskNumber = ChangeDateCommand.parseTaskNumber(commandArguments[0]);
        if (taskNumber == -1) {
            return;
        }
        //find the corresponding task with this task-number.
        Task currentTask;
        currentTask = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
        if (currentTask == null) {
            System.err.println(TaskNotExistsException.MESSAGE);
            return;
        }

        //toggle tasks
        List<Task> tasks = new LinkedList<>();
        tasks.add(currentTask);
        toggle(tasks, !currentTask.isFinished());

        System.out.printf((TOGGLE_SUCCEED_OUTPUT) + System.lineSeparator(), currentTask.getTaskName(),
                getNumberOfDirectedAndIndirectSubtasks(currentTask));
    }

    private void toggle(List<Task> tasks, boolean taskStatus) {
        for (Task task : tasks) {
            task.setTaskStatus(taskStatus);
            if (!task.getSubtask().isEmpty()) {
                toggle(task.getSubtask(), taskStatus);
            }
        }
    }
}
