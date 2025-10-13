package edu.kit.kastel.procrastinot;

import edu.kit.kastel.procrastinot.model.ProcrastinotApp;
import edu.kit.kastel.procrastinot.ui.CommandHandler;

/**
 * Main class of the program.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public final class Main {
    private Main() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * Main entry point of the program.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        ProcrastinotApp procrastinotApp = new ProcrastinotApp();
        CommandHandler commandHandler = new CommandHandler(procrastinotApp);
        commandHandler.handleUserInput();
    }
}
