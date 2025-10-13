package edu.kit.kastel.procrastinot.model;
/**
 * Exception throw when this task doesn't exist in the trash.
 *
 * @author uqeoc
 * @author Programmieren-Team
 * @version 1.0
 */
public class TaskNotExistsInTheTrashException extends Exception {
    /**
     * Task doesn't exist in the trash error.
     */
    public static final String MESSAGE = "ERROR: This task doesn't exist in the trash.";

    /**
     * Instantiates a new task not found in the trash exception.
     */
    public TaskNotExistsInTheTrashException() {
        super(MESSAGE);
    }
}
