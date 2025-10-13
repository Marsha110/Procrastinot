package edu.kit.kastel.procrastinot.ui;

import java.util.Objects;

/**
 * A command that can be executed by the user.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public abstract class Command {

    /**
     * The command handler for this command
     */
    protected final CommandHandler commandHandler;
    private final String commandName;

    /**
     * Instantiates a new command.
     *
     * @param commandName    the command name
     * @param commandHandler the command handler for this command
     */
    protected Command(String commandName, CommandHandler commandHandler) {
        this.commandName = Objects.requireNonNull(commandName);
        this.commandHandler = Objects.requireNonNull(commandHandler);
    }

    /**
     * Gets the command name.
     *
     * @return the command name
     */
    public final String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param commandArguments the command arguments
     */
    public abstract void execute(String[] commandArguments);
}

