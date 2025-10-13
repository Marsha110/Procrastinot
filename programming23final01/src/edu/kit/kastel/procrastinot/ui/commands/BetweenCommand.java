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
 * Command to find tasks between two specified dats.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class BetweenCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "between";
    private static final String ERROR_DATE_FORMAT = "ERROR: Date format is wrong, it shall be year-month-day.";
    private static final int EXPECTED_ARGUMENTS_LENGTH = 2;

    /**
     * Instantiates a new between command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public BetweenCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_ARGUMENTS_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(commandArguments[0]);
            endDate = LocalDate.parse(commandArguments[1]);
        } catch (DateTimeException e) {
            System.err.println(ERROR_DATE_FORMAT);
            return;
        }
        System.out.println(procrastinotApp.showTasksInfo(tasksBetweenDate(procrastinotApp
                .getTaskTopLevel(), startDate, endDate)));
    }

    /**
     * Show the information of all tasks which between two dates.
     *
     * @param startTask the start-task to be searched.
     * @param startDate the start-date to be searched.
     * @param endDate   the end-date to be searched.
     * @return The information of all tasks which between these two dates.
     */
    public List<Task> tasksBetweenDate(Task startTask, LocalDate startDate, LocalDate endDate) {
        List<Task> tasks = new LinkedList<>();
        LocalDate deadline = startTask.getDeadline();
        if (deadline != null && deadline.isBefore(endDate.plusDays(1))
                && deadline.isAfter(startDate.minusDays(1))) {
            tasks.add(startTask);
        }
        if (startTask.hasSubtask()) {
            startTask.getSubtask().forEach(task -> tasks.addAll(tasksBetweenDate(task, startDate, endDate)));
        }
        return tasks;
    }
}
