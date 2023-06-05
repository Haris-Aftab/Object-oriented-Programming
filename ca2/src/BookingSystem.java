import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Comparator;

/**
 * This class allows the manipulates the rooms, assistants and bookings
 */
public class BookingSystem {
    private ArrayList<BookableRoom> bookableRooms = new ArrayList<>();
    private ArrayList<AssistantOnShift> assistantOnShifts = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();

    public ArrayList<BookableRoom> getBookableRooms() { return bookableRooms; }
    public ArrayList<AssistantOnShift> getAssistantOnShifts() { return assistantOnShifts; }
    public ArrayList<Booking> getBookings() { return bookings; }

    /**
     * Adds a bookable room to the uni
     * @param room A room belonging to the uni
     * @param date A date that the room is booked for
     */
    public void addBookableRoom(Room room, Date date) {
        bookableRooms.add(new BookableRoom(room, date));
        getBookableRooms().sort(Comparator.comparing(BookableRoom::getDate)); // sorts rooms by date
    }

    /**
     * Removes bookable room from the system
     * @param bookableRoom A bookable room belonging to the uni
     */
    public void removeBookableRoom(BookableRoom bookableRoom) { bookableRooms.remove(bookableRoom); }

    /**
     * Lists bookable rooms
     * @return bookable rooms
     */
    public String showBookableRoom() {
        StringBuilder s = new StringBuilder("List of bookable rooms: ");
        int sequentialID = 10;
        for (BookableRoom bookableRoom : bookableRooms) {
            sequentialID++;
            s.append("\n%d. %s".formatted(sequentialID, bookableRoom));
        }
        return s.toString();
    }

    /**
     * Checks if any room is available
     * @return Returns boolean true if a room is available
     */
    public boolean roomAvailable() {
        if (bookableRooms.size() == 0) return false;
        for (BookableRoom bookableRoom : bookableRooms) {
            if (bookableRoom.getStatus() != RoomStatus.FULL) return true;
        }
        return false;
    }

    /**
     * Adds an assistant to the uni for the day
     * @param assistant An assistant belonging to the uni
     * @param date A day the assistant in booked for
     */
    public void addAssistantOnShift(Assistant assistant, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setLenient(false);
        try {
            Date shift1 = sdf.parse(date+" "+"07:00");
            Date shift2 = sdf.parse(date+" "+"08:00");
            Date shift3 = sdf.parse(date+" "+"09:00");
             // adds assistant for each hour
            assistantOnShifts.add(new AssistantOnShift(assistant, shift1));
            assistantOnShifts.add(new AssistantOnShift(assistant, shift2));
            assistantOnShifts.add(new AssistantOnShift(assistant, shift3));
            getAssistantOnShifts().sort(Comparator.comparing(AssistantOnShift::getDate)); // sorts assistant by date
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes assistant from shift
     * @param assistantOnShift An Assistant working a particular shift
     */
    public void removeAssistantOnShift(AssistantOnShift assistantOnShift) { assistantOnShifts.remove(assistantOnShift); }

    /**
     * Lists assistants on shift
     * @return list of assistnats of shift
     */
    public String showAssistantOnShift() {
        StringBuilder s = new StringBuilder("List of assistants on shift: ");
        int sequentialID = 10;
        for (AssistantOnShift assistantOnShift : assistantOnShifts) {
            sequentialID++;
            s.append("\n%d. %s".formatted(sequentialID, assistantOnShift));
        }
        return s.toString();
    }

    /**
     * Checks if any assistant is available
     * @return Returns boolean true if an assistant is free
     */
    public boolean assistantAvailable() {
        if (assistantOnShifts.size() == 0) return false;
        for (AssistantOnShift assistantOnShift : assistantOnShifts) {
            if (assistantOnShift.getStatus() == AssistantStatus.FREE) return true;
        }
        return false;
    }

    /**
     * Adds a booking to the system
     * @param bookableRoom A non full bookable room
     * @param assistantOnShift A free assistant on shift
     * @param date A common date between bookable room and assistant on shift
     * @param studentEmail A student email
     */
    public void addBooking(BookableRoom bookableRoom, AssistantOnShift assistantOnShift, Date date, String studentEmail) {
        bookings.add(new Booking(bookableRoom, assistantOnShift, date, studentEmail));
    }

    /**
     * Removes booking from the system
     * @param booking A non completed booking
     */
    public void removeBooking(Booking booking) {
        booking.getBookableRoom().removeOccupancy();
        booking.getAssistantOnShift().setStatus(AssistantStatus.FREE);
        bookings.remove(booking);

    }

    /**
     * Lists the bookings in the system
     * @return A list of bookings
     */
    public String showBooking() {
        StringBuilder s = new StringBuilder("List of bookings: ");
        int sequentialID = 10;
        for (Booking booking : bookings) {
            sequentialID++;
            s.append("\n%d. %s".formatted(sequentialID, booking));
        }
        return s.toString();
    }
}