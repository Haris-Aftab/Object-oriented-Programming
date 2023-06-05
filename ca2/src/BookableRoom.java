import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Status of a bookable room
 */
enum RoomStatus {EMPTY, AVAILABLE, FULL}

/**
 * This class creates a bookable room
 */
public class BookableRoom {
    private final Room room;
    private final Date date;
    private RoomStatus status;
    private int occupancy;
    private final String roomCode;

    /**
     * Increases occupancy
     */
    public void addOccupancy() { occupancy += 1; setStatus(); }

    /**
     * Decreases occupancy
     */
    public void removeOccupancy() { occupancy -= 1; setStatus(); }

    /**
     * Sets status of a room
     */
    public void setStatus() {
        if (occupancy == 0) status = RoomStatus.EMPTY;
        else if (occupancy == room.getCapacity()) status = RoomStatus.FULL;
        else status = RoomStatus.AVAILABLE;
    }

    /**
     * Gets date of bookable room
     * @return Date of bookable room
     */
    public Date getDate() { return date; }

    /**
     * Gets status of bookable room
     * @return Status of bookable room
     */
    public RoomStatus getStatus() { return status; }

    /**
     * Gets room code of bookable room
     * @return Room code of bookable room
     */
    public String getRoomCode() { return roomCode; }

    /**
     * Bookable room constructor
     * @param room A room to make bookable
     * @param date A date for the room to be made bookable
     */
    public BookableRoom(Room room, Date date) {
        this.room = room;
        this.date = date;
        roomCode = "%s_%s".formatted(room.getCode(), date.toString().substring(11,13));
        setStatus();
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "| %s | %s | %s | occupancy: %d |".formatted(formatter.format(date), status, roomCode, occupancy);
    }
}
