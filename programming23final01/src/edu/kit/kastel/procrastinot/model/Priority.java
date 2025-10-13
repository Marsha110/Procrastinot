package edu.kit.kastel.procrastinot.model;
/**
 * The type of priority.
 *
 * @author uqeoc
 * @version 1.0
 */
public enum Priority {
    /**
     * High priority.
     */
    HI,
    /**
     * Middle priority.
     */
    MD,
    /**
     * Low priority.
     */
    LO;

    /**
     * Find corresponding priority according to the name of priority.
     * @param priorityInput the name of the level of priority.
     * @return  corresponding enum priority if it has. Otherwise, return null.
     */
    public static Priority findPriority(String priorityInput) {
        for (Priority priority : Priority.values()) {
            if (priority.name().equals(priorityInput)) {
                return priority;
            }
        }
        return null;
    }
}
