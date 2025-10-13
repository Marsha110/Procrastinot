package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.model.TaskList;
import edu.kit.kastel.procrastinot.model.TaskNotExistsException;
import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

/**
 * Command to mark the tag of a list or a task.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class TagCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "tag";
    private static final String ERROR_TAG_DUPLICATE = "ERROR: The task-tag is duplicated.";
    private static final String ERROR_TASK_LIST_NOT_EXIST = "ERROR: The task-list doesn't exist.";
    private static final String TAG_NAME_REGEX = "^[A-Za-z0-9]+$";
    private static final String ERROR_TAG_NAME_FORMAT = "ERROR: The list-name can only contain A-Z or a-z.";
    private static final String TAG_SUCCEED_OUTPUT = "tagged %s with %s";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;

    /**
     * Instantiates a new tag command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public TagCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        //parse tag.
        String tag;
        if (commandArguments[1].matches(TAG_NAME_REGEX)) {
            tag = commandArguments[1];
        } else {
            System.err.println(ERROR_TAG_NAME_FORMAT);
            return;
        }

        //parse the task-number
        int taskNumber = -1;
        boolean isTaskNumber = true;
        try {
            taskNumber = Integer.parseInt(commandArguments[0]);
        } catch (NumberFormatException e) {
            isTaskNumber = false;
        }

        //Set tag for a list.
        Task task;
        if (!isTaskNumber) {
            TaskList taskList = procrastinotApp.findTaskList(commandArguments[0]);
            if (taskList == null) {
                System.err.println(ERROR_TASK_LIST_NOT_EXIST);
            } else {
                if (taskList.setTag(commandArguments[1])) {
                    System.out.printf((TAG_SUCCEED_OUTPUT) + System.lineSeparator(), taskList.getListName(), taskList.getTag());
                } else {
                    System.err.println(ERROR_TAG_DUPLICATE);
                }
            }
        } else {
            //Set tag for a task.
            task = procrastinotApp.findTask(procrastinotApp.getTaskTopLevel(), taskNumber);
            if (task == null) {
                System.err.println(TaskNotExistsException.MESSAGE);
                return;
            }

            //verify if the tag is duplicated.
            if (!task.addTag(tag)) {
                System.err.println(ERROR_TAG_DUPLICATE);
                return;
            }
            System.out.printf((TAG_SUCCEED_OUTPUT) + System.lineSeparator(), task.getTaskName(), tag);
        }
    }
}
