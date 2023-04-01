package exceptions;

/**
 * This exception is thrown when an attempt is made to schedule an appointment at a time slot that is already
 * occupied by another appointment. It indicates a scheduling conflict and alerts the user to choose
 * an available time slot to avoid overlapping appointments.
 */
public class AppointmentOverlap extends RuntimeException {

    /**
     * Constructs an instance of the AppointmentOverlapException class with a specified cause.
     * This exception indicates that an appointment is overlapping with another appointment,
     * causing a conflict that needs to be resolved.
     *
     * @param cause the Throwable object that caused the exception to be thrown.
     *              This can be any type of exception or error that occurred during the execution of the program.
     */
    public AppointmentOverlap(Throwable cause) {
        super(cause);
    }
}
