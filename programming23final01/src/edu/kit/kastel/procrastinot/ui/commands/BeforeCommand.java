package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Command to find tasks before a specified date.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class BeforeCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "before";
    private static final String ERROR_DATE_FORMAT = "ERROR: Date format is wrong, it shall be year-month-day.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 1;

    /**
     * Instantiates a new before command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public BeforeCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        LocalDate date;
        try {
            date = LocalDate.parse(commandArguments[0]);
        } catch (DateTimeException e) {
            System.err.println(ERROR_DATE_FORMAT);
            return;
        }
        System.out.println(procrastinotApp.showTasksInfo(taskDeadlineBefore(procrastinotApp
                .getTaskTopLevel(), date)));
    }

    private List<Task> taskDeadlineBefore(Task startTask, LocalDate deadline) {
        List<Task> tasks = new LinkedList<>();
        if (startTask.getDeadline() != null && startTask.getDeadline().isBefore(deadline.plusDays(1))) {
            tasks.add(startTask);
        }
        if (startTask.hasSubtask()) {
            startTask.getSubtask().forEach(task -> tasks.addAll(taskDeadlineBefore(task, deadline)));
        }
        return tasks;
    }
}
