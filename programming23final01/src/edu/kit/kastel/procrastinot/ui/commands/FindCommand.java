package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.util.LinkedList;
import java.util.List;

/**
 * Command to find all tasks which name has a specified partial word.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class FindCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "find";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new find command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public FindCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        System.out.println(showTasksWithPartialWord(commandArguments[0]));
    }

    private String showTasksWithPartialWord(String partialWord) {
        return procrastinotApp.showTasksInfo(getTasksWithPartialWord(procrastinotApp.getTaskTopLevel(), partialWord));
    }

    private List<Task> getTasksWithPartialWord(Task startTask, String partialWord) {
        List<Task> tasksWithPartialWord = new LinkedList<>();
        if (startTask.getTaskName().contains(partialWord)) {
            tasksWithPartialWord.add(startTask);
        }
        if (startTask.hasSubtask()) {
            startTask.getSubtask().forEach(task
                    -> tasksWithPartialWord.addAll(getTasksWithPartialWord(task, partialWord)));
        }
        return tasksWithPartialWord;
    }

}
