package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;
/**
 * Command to delete a task and all its subtasks.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class DeleteCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "delete";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final String DELETE_SUCCEED_OUTPUT = "deleted %s and %d subtasks";

    /**
     * Instantiates a new delete command.
     *
     * @param commandHandler the command handler
     * @param procrastinotApp        a procrastinot-APP
     */
    public DeleteCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {

        //parse the task-number
        int taskNumber = ChangeDateCommand.parseTaskNumber(commandArguments[0]);
        if (taskNumber == -1) {
            return;
        }

        //Get the task from task-number.
        Task task;
        task = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
        if (task == null) {
            System.err.println(TaskNotExistsException.MESSAGE);
            return;
        }

        //delete the task.
        try {
            deleteTask(taskNumber);
        } catch (TaskNotExistsException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.printf((DELETE_SUCCEED_OUTPUT) + System.lineSeparator(),
                task.getTaskName(), RestoreCommand.getNumberOfDirectedAndIndirectSubtasks(task));
    }

    private void deleteTask(int taskNumber) throws TaskNotExistsException {
        Task taskToBeDeleted = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
        if (taskToBeDeleted != null) {
            taskToBeDeleted.removeTaskAndAllDirectAndIndirectSubtasksFromAllItsTaskLists();
            procrastinotApp.addTaskToTrash(taskToBeDeleted);
        } else {
            throw new TaskNotExistsException();
        }
    }
}
