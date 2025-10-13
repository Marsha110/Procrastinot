package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskList;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

/**
 * Command to assign a task to a task or assign a task to a list.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class AssignCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "assign";
    private static final String ERROR_TASK_NUMBER_FORMAT_ERROR =
            "ERROR: The format of the task-number is wrong, it shall be a integer.";
    private static final String ASSIGN_SUCCEED_OUTPUT = "assigned %s to %s";
    private static final String ERROR_ASSIGNED_TASK_TO_ITSELF = "ERROR: Can't assign a task to itself.";
    private static final String ERROR_TASK_ALREADY_ASSIGNED = "ERROR: Task %d is already assigned to task %d.";
    private static final String ERROR_TASK_ALREADY_ASSIGNED_TO_TASK_LIST
            = "ERROR: Task %d is already assigned to task-list %s.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;

    /**
     * Instantiates a new assign command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public AssignCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        //check if it needs to assign a task to a task-list.
        int taskNumber;
        TaskList taskList = procrastinotApp.findTaskList(commandArguments[1]);

        //assign task to a list.
        if (taskList != null) {
            try {
                taskNumber = Integer.parseInt(commandArguments[0]);
            } catch (NumberFormatException e) {
                System.err.println(ERROR_TASK_NUMBER_FORMAT_ERROR);
                return;
            }
            Task task;
            task = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
            if (task == null) {
                System.err.println(TaskNotExistsException.MESSAGE);
                return;
            }
            if (!taskList.contain(task)) {
                taskList.addTaskToList(task);
                System.out.printf((ASSIGN_SUCCEED_OUTPUT) + System.lineSeparator(),
                        task.getTaskName(), taskList.getListName());
            } else {
                System.err.printf(ERROR_TASK_ALREADY_ASSIGNED_TO_TASK_LIST
                        + System.lineSeparator(), task.getTaskNumber(), taskList.getListName());
            }
        } else {
            //check if it needs to assign a task to another task.
            int subTaskNumber;
            int parentTaskNumber;
            try {
                subTaskNumber = Integer.parseInt(commandArguments[0]);
                parentTaskNumber = Integer.parseInt(commandArguments[1]);
            } catch (NumberFormatException e) {
                System.err.println(ERROR_TASK_NUMBER_FORMAT_ERROR);
                return;
            }
            assignTaskToTask(parentTaskNumber, subTaskNumber);
        }
    }

    private void assignTaskToTask(int parentTaskNumber, int subTaskNumber) {

        if (parentTaskNumber == subTaskNumber) {
            System.err.println(ERROR_ASSIGNED_TASK_TO_ITSELF);
            return;
        }

        Task parentTask;
        Task subTask;
        parentTask = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), parentTaskNumber);
        subTask = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), subTaskNumber);

        if (parentTask == null || subTask == null) {
            System.err.println(TaskNotExistsException.MESSAGE);
            return;
        }

        if (!parentTask.getSubtask().contains(subTask)) {
            parentTask.addASubtask(subTask);
            System.out.printf((ASSIGN_SUCCEED_OUTPUT) + System.lineSeparator(),
                    subTask.getTaskName(), parentTask.getTaskName());
        } else {
            System.err.printf((ERROR_TASK_ALREADY_ASSIGNED) + System.lineSeparator(), subTaskNumber, parentTaskNumber);
        }
    }
}
