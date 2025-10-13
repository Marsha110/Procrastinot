package edu.kit.kastel.procrastinot.ui.commands;

import edu.kit.kastel.procrastinot.ui.Command;
import edu.kit.kastel.procrastinot.ui.CommandHandler;

/**
 * Command to quit the program.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class QuitCommand extends Command {

    private static final String COMMAND_NAME = "quit";
    private static final String QUIT_WITH_ARGUMENTS_ERROR = "ERROR: quit does not allow args.";

    /**
     * Instantiates a new quit command.
     *
     * @param commandHandler the command handler
     */
    public QuitCommand(CommandHandler commandHandler) {
        super(COMMAND_NAME, commandHandler);
    }

    @Override
    public void execute(String[] commandArguments) {
        if (commandArguments.length != 0) {
            System.err.println(QUIT_WITH_ARGUMENTS_ERROR);
            return;
        }
        commandHandler.quit();
    }
}
