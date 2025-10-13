package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.model.Task;
import edu.kit.kastel.procrastinot.ui.CommandHandler;
import edu.kit.kastel.procrastinot.ui.ProcrastinotAppCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Command to show all tasks which deadline in 7 days.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class UpcomingCommand extends ProcrastinotAppCommand {
    private static final String COMMAND_NAME = "upcoming";
    private static final String ERROR_DATE_FORMAT = "ERROR: Date format is wrong, it shall be year-month-day.";
    private static final int SEVEN_DAYS = 7;
    private static final int EXPECTED_DATE_LENGTH = 1;

    /**
     * Instantiates a new add command.
     *
     * @param commandHandler  the command handler
     * @param procrastinotApp a procrastinot-APP
     */
    public UpcomingCommand(CommandHandler commandHandler, ProcrastinotApp procrastinotApp) {
        super(COMMAND_NAME, commandHandler, procrastinotApp, EXPECTED_DATE_LENGTH);
    }

    @Override
    protected void executeProcrastinotAppCommand(String[] commandArguments) {
        LocalDate deadline;
        try {
            deadline = LocalDate.parse(commandArguments[0]);
        } catch (DateTimeParseException e) {
            System.err.println(ERROR_DATE_FORMAT);
            return;
        }
        System.out.println(procrastinotApp.showTasksInfo(showUpcomingTasks(procrastinotApp.getTaskTopLevel(), deadline)));
    }

    private List<Task> showUpcomingTasks(Task startTask, LocalDate date) {
        LocalDate lastDayOfUpcomingDate = date.plusDays(SEVEN_DAYS);
        List<Task> exploredTasks = new LinkedList<>();
        if (startTask.getDeadline() != null && startTask.getDeadline().isAfter(date.minusDays(1))
                && startTask.getDeadline().isBefore(lastDayOfUpcomingDate)) {
            exploredTasks.add(startTask);
        }
        if (startTask.hasSubtask()) {
            for (Task task : startTask.getSubtask()) {
                exploredTasks.addAll(showUpcomingTasks(task, date));
            }
        }
        return exploredTasks;
    }
}
