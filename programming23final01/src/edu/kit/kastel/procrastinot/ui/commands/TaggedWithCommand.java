package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.LinkedList;
import java.util.List;

/**
 * Command to find all the tasks with a given tag.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class TaggedWithCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "tagged-with";
    private static final String ERROR_TAG_FORMAT = "ERROR: Tag shall consist of A-Z or a-z or 0-9.";
    private static final String LIST_NAME_REGEX = "^[A-Za-z0-9]+$";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new tagged-with command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public TaggedWithCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        //parse tag.
        String tag;
        if (commandArguments[0].matches(LIST_NAME_REGEX)) {
            tag = commandArguments[0];
        } else {
            System.err.println(ERROR_TAG_FORMAT);
            return;
        }

        String taggedTaskInfo = procrastinotApp.showTasksInfo(findTaskWithATag(procrastinotApp.getTaskTopLevel(), tag));
        System.out.println(taggedTaskInfo);
    }

    private List<Task> findTaskWithATag(Task startTask, String tag) {
        List<Task> taggedTask = new LinkedList<>();
        if (startTask.getTags().contains(tag)) {
            taggedTask.add(startTask);
        }
        if (startTask.hasSubtask()) {
            for (Task task : startTask.getSubtask()) {
                taggedTask.addAll(findTaskWithATag(task, tag));
            }
        }
        return taggedTask;
    }
}
