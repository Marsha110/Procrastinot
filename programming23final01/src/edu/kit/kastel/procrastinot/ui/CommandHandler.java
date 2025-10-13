package edu.kit.kastel.procrastinot.ui;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.commands.AddCommand;
import edu.kit.kastel.procrastinot.ui.commands.AddListCommand;
import edu.kit.kastel.procrastinot.ui.commands.AssignCommand;
import edu.kit.kastel.procrastinot.ui.commands.BeforeCommand;
import edu.kit.kastel.procrastinot.ui.commands.BetweenCommand;
import edu.kit.kastel.procrastinot.ui.commands.ChangeDateCommand;
import edu.kit.kastel.procrastinot.ui.commands.ChangePriorityCommand;
import edu.kit.kastel.procrastinot.ui.commands.DeleteCommand;
import edu.kit.kastel.procrastinot.ui.commands.DuplicatesCommand;
import edu.kit.kastel.procrastinot.ui.commands.FindCommand;
import edu.kit.kastel.procrastinot.ui.commands.ListCommand;
import edu.kit.kastel.procrastinot.ui.commands.ToDoCommand;
import edu.kit.kastel.procrastinot.ui.commands.QuitCommand;
import edu.kit.kastel.procrastinot.ui.commands.RestoreCommand;
import edu.kit.kastel.procrastinot.ui.commands.ShowCommand;
import edu.kit.kastel.procrastinot.ui.commands.TagCommand;
import edu.kit.kastel.procrastinot.ui.commands.TaggedWithCommand;
import edu.kit.kastel.procrastinot.ui.commands.ToggleCommand;
import edu.kit.kastel.procrastinot.ui.commands.UpcomingCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * The command handler responsible for maintaining all necessary commands and user interaction.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class CommandHandler {
    private static final String COMMAND_SEPARATOR_REGEX = "\\s+";
    private static final String COMMAND_NOT_FOUND = "ERROR: Command '%s' not found";
    private final ProcrastinotApp procrastinotApp;
    private boolean running = false;
    private final Map<String, Command> commands;

    /**
     * Instantiates a new command handler.
     *
     * @param procrastinotApp a procrastinotApp
     */
    public CommandHandler(ProcrastinotApp procrastinotApp) {
        this.procrastinotApp = Objects.requireNonNull(procrastinotApp);
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Handles the user input.
     */
    public void handleUserInput() {
        this.running = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Quits the user input handling.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);

        if (!commands.containsKey(commandName)) {
            System.err.printf((COMMAND_NOT_FOUND) + System.lineSeparator(), commandName);
            return;
        }
        commands.get(commandName).execute(commandArguments);
    }

    private void initCommands() {
        this.addCommand(new QuitCommand(this));
        this.addCommand(new AddCommand(this, procrastinotApp));
        this.addCommand(new ToggleCommand(this, procrastinotApp));
        this.addCommand(new AssignCommand(this, procrastinotApp));
        this.addCommand(new AddListCommand(this, procrastinotApp));
        this.addCommand(new TagCommand(this, procrastinotApp));
        this.addCommand(new ShowCommand(this, procrastinotApp));
        this.addCommand(new ChangeDateCommand(this, procrastinotApp));
        this.addCommand(new UpcomingCommand(this, procrastinotApp));
        this.addCommand(new ListCommand(this, procrastinotApp));
        this.addCommand(new DeleteCommand(this, procrastinotApp));
        this.addCommand(new RestoreCommand(this, procrastinotApp));
        this.addCommand(new TaggedWithCommand(this, procrastinotApp));
        this.addCommand(new ToDoCommand(this, procrastinotApp));
        this.addCommand(new DuplicatesCommand(this, procrastinotApp));
        this.addCommand(new BeforeCommand(this, procrastinotApp));
        this.addCommand(new ChangePriorityCommand(this, procrastinotApp));
        this.addCommand(new FindCommand(this, procrastinotApp));
        this.addCommand(new BetweenCommand(this, procrastinotApp));
    }

    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }
}
