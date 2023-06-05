/**
 * Sets up room
 */
public class Room {
    private final String code;
    private final int capacity;

    /**
     * Gets the room code
     * @return Room code
     */
    public String getCode() { return code; }

    /**
     * Gets room capacity
     * @return Room capacity
     */
    public int getCapacity() { return capacity; }

    /**
     * Room constructor
     * @param code Room code
     * @param capacity Room capacity
     */
    public Room(String code, int capacity){
        this.code = code;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "| %s | capacity: %d |".formatted(code, capacity);
    }
}
