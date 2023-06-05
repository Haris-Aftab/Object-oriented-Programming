import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Status of Booking
 */
enum BookingStatus {SCHEDULED, COMPLETED}

/**
 * This class creates a booking 
 */
public class Booking {
    private final BookableRoom bookableRoom;
    private final AssistantOnShift assistantOnShift;
    private final Date date;
    private final String studentEmail;
    private final String bookingID;
    private BookingStatus status;

    /**
     * Sets status of a booking
     * @param status Status of booking
     */
    public void setStatus(BookingStatus status) { this.status = status; }
    public SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Get the bookable room in the booking
     * @return Bookable room
     */
    public BookableRoom getBookableRoom() { return bookableRoom; }

    /**
     * Gets the assistant on shift in the booking
     * @return Assistant on shift
     */
    public AssistantOnShift getAssistantOnShift() { return assistantOnShift; }

    /**
     * Gets status of booking
     * @return Status of booking
     */
    public BookingStatus getStatus() { return status; }

    /**
     * Booking constructor
     * @param bookableRoom Bookable room to be booked
     * @param assistantOnShift Assistant on shift to be booked
     * @param date Date the room and assistant are booked for, for the student
     * @param studentEmail Student email
     */
    public Booking(BookableRoom bookableRoom, AssistantOnShift assistantOnShift, Date date, String studentEmail) {
        this.bookableRoom = bookableRoom;
        this.assistantOnShift = assistantOnShift;
        this.date = date;
        this.studentEmail = studentEmail;
        bookingID = "%s_%s_%sAM".formatted(bookableRoom.getRoomCode(), formatter.format(date).substring(0,10), formatter.format(date).substring(11,14));
        bookableRoom.addOccupancy();
        assistantOnShift.setStatus(AssistantStatus.BUSY);
        status = BookingStatus.SCHEDULED;
    }

    @Override
    public String toString() {
        return "| %s | %s | %s | %s | %s |".formatted(formatter.format(date), status, getAssistantOnShift().getEmail(), getBookableRoom().getRoomCode(), studentEmail);
    }
}
