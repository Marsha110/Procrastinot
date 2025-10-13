package edu.kit.kastel.procrastinot.ui;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A command used for handling {@link ProcrastinotApp} regarding interactions.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public abstract class ProcrastinotAppCommand extends Command {
    private static final String EXPECTED_INNER_ARGUMENTS_ERROR_FORMAT = "ERROR: Expected %d arguments but got %d";
    private static final String TODO_COMMAND_SEPARATOR = "\\s+";
    protected final ProcrastinotApp procrastinotApp;
    private final int expectedNumberOfArguments;


    /**
     * Creates a new procrastinotApp command.
     *
     * @param commandName               The name of the command.
     * @param commandHandler            The command handler.
     * @param procrastinotApp           a procrastinot-APP
     * @param expectedNumberOfArguments The expected number of arguments.
     */
    protected ProcrastinotAppCommand(String commandName, CommandHandler commandHandler,
                                     ProcrastinotApp procrastinotApp, int expectedNumberOfArguments) {
        super(commandName, commandHandler);
        this.procrastinotApp = Objects.requireNonNull(procrastinotApp);
        this.expectedNumberOfArguments = expectedNumberOfArguments;
    }

    @Override
    public final void execute(String[] commandArguments) {
        List<String> argumentsForProcrastinotAppCommand = new ArrayList<>();
        for (String commandArgument : commandArguments) {
            argumentsForProcrastinotAppCommand
                    .addAll(Arrays.asList(commandArgument.split(TODO_COMMAND_SEPARATOR)));
        }
        if (argumentsForProcrastinotAppCommand.size() != expectedNumberOfArguments) {
            System.err.printf(EXPECTED_INNER_ARGUMENTS_ERROR_FORMAT + System.lineSeparator(), expectedNumberOfArguments,
                    argumentsForProcrastinotAppCommand.size());
            return;
        }
        executeProcrastinotAppCommand(commandArguments);
    }

    /**
     * Executes the command on the procrastinotApp.
     *
     * @param commandArguments The command arguments.
     */
    protected abstract void executeProcrastinotAppCommand(String[] commandArguments);
}
