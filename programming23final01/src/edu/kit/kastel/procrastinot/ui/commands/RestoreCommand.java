package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.TaskNotExistsInTheTrashException;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.List;

/**
 * Command to restore a task.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class RestoreCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "restore";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;
    private static final String RESTORE_SUCCEED_OUTPUT = "restored %s and %d subtasks";

    /**
     * Instantiates a new restore command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public RestoreCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        //parse the task-number
        int taskNumber = ChangeDateCommand.parseTaskNumber(commandArguments[0]);
        if (taskNumber == -1) {
            return;
        }

        //restore the task from trash.
        Task task;
        try {
            task = restoreTaskAndAllSubtasks(taskNumber);
        } catch (TaskNotExistsInTheTrashException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.printf((RESTORE_SUCCEED_OUTPUT) + System.lineSeparator(),
                task.getTaskName(), getNumberOfDirectedAndIndirectSubtasks(task));
    }

    /**
     * Get the number of all directed and indirect subtasks.
     *
     * @param task A task.
     * @return the number of all directed and indirect subtasks.
     */
    public static int getNumberOfDirectedAndIndirectSubtasks(Task task) {
        int count = 0;
        List<Task> subtasks = task.getSubtask();
        if (!subtasks.isEmpty()) {
            for (Task subtask : subtasks) {
                count++;
                count += getNumberOfDirectedAndIndirectSubtasks(subtask);
            }
        }
        return count;
    }

    private Task restoreTaskAndAllSubtasks(int taskNumber) throws TaskNotExistsInTheTrashException {
        Task task = procrastinotApp.getTaskInTrash(taskNumber);
        if (task == null) {
            throw new TaskNotExistsInTheTrashException();
        }
        restore(task);
        return task;
    }

    /**
     * restore this task to all its task-list and its parent.
     */
    private void restore(Task task) {

        //restore task to its lists.
        if (!task.getTaskLists().isEmpty()) {
            task.getTaskLists().forEach(taskList -> taskList.addTaskToList(task));
        }

        //restore task to its parent if the parent hasn't been deleted
        // or delete task from his parent if the parent has been deleted.
        if (procrastinotApp.getTaskInTrash(task.getParent().getTaskNumber()) != null) {
            task.getParent().removeASubtask(task);
            procrastinotApp.getTaskTopLevel().addASubtask(task);
            task.setParent(procrastinotApp.getTaskTopLevel());
        } else {
            task.getParent().addASubtask(task);
        }

        //if the root of trash contains this task, then remove it from the root of the trash.
        procrastinotApp.removeTaskFromTrash(task);
    }
}
