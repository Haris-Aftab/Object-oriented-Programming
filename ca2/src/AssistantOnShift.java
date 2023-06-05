import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Status of assistant on shift
 */
enum AssistantStatus {FREE, BUSY}

/**
 * This class creates an assistant on shift
 */
public class AssistantOnShift {
    private final Assistant assistant;
    private final Date date;
    private AssistantStatus status;
    private final String email;

    /**
     * Sets status of an assistant on shift
     * @param status
     */
    public void setStatus(AssistantStatus status) { this.status = status; }

    /**
     * Gets date of assistant on shift
     * @return Date assistant on shift
     */
    public Date getDate() { return date; }

    /**
     * Gets status of assistant on shift
     * @return Status of assistant on shift
     */
    public AssistantStatus getStatus() { return status; }

    /**
     * Gets email of assistant on shift
     * @return Email of assistant on shift
     */
    public String getEmail() { return email; }

    /**
     * Assistant on shift constructor
     * @param assistant An assistant to add to a shift
     * @param date A date for the assistant to have their shift
     */
    public AssistantOnShift(Assistant assistant, Date date) {
        this.assistant = assistant;
        this.date = date;
        email = assistant.getEmail();
        status = AssistantStatus.FREE;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "| %s | %s | %s |".formatted(formatter.format(date), status, email);
    }
}
