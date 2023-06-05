/**
 * Sets up the uni resources
 */
public class UniversityResources {
    private Assistant[] assistants;
    private Room[] rooms;

    /**
     * Sets assistants
     * @param assistants List of assistants
     */
    public void setAssistants(Assistant[] assistants) { this.assistants = assistants; }

    /**
     * Sets rooms
     * @param rooms List of rooms
     */
    public void setRooms(Room[] rooms) { this.rooms = rooms; }

    /**
     * Gets assistants
     * @return Returns assistants array
     */
    public Assistant[] getAssistants() { return assistants; }

    /**
     * Gets rooms
     * @return Returns rooms array
     */
    public Room[] getRooms() { return rooms; }

    /**
     * Shows rooms as list
     * @return Rooms
     */
    public String showRooms() {
        StringBuilder s = new StringBuilder("List of rooms: ");
        int sequentialID = 10;
        for (Room room : rooms) {
            sequentialID++;
            s.append("\n%d. %s".formatted(sequentialID, room));
        }
        return s.toString();
    }

    /**
     * Shows assistant as list
     * @return Assistants
     */
    public String showAssistants() {
        StringBuilder s = new StringBuilder("List of assistants: ");
        int sequentialID = 10;
        for (Assistant assistant : assistants) {
            sequentialID++;
            s.append("\n%d. %s".formatted(sequentialID, assistant));
        }
        return s.toString();
    }

    /**
     * Adds assistants to the uni
     */
    public void addAssistance() {
        Assistant a1 = new Assistant("Alex Adams", "alex@uok.ac.uk");
        Assistant a2 = new Assistant("Billy Brown", "billy@uok.ac.uk");
        Assistant a3 = new Assistant("Chris Clark", "chris@uok.ac.uk");
        Assistant a4 = new Assistant("Dave Down", "dave@uok.ac.uk");
        setAssistants(new Assistant[]{a1, a2, a3, a4});
    }

    /**
     * Adds rooms to the uni
     */
    public void addRooms() {
        Room r1 = new Room("Room1", 2);
        Room r2 = new Room("Room2", 3);
        Room r3 = new Room("Room3", 3);
        Room r4 = new Room("Room4", 6);
        setRooms(new Room[]{r1, r2, r3, r4});
    }
}
