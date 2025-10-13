package edu.kit.kastel.procrastinot.model;
/**
 * Exception throw when this task doesn't exist.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class TaskNotExistsException extends Exception {
    /**
     * Task doesn't exist error.
     */
    public static final String MESSAGE = "ERROR: This task doesn't exist.";

    /**
     * Instantiates a new task not found exception.
     */
    public TaskNotExistsException() {
        super(MESSAGE);
    }
}
