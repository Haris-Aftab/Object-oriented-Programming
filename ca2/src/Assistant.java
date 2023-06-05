/**
 * Sets up Assistant
 */
public class Assistant {
    private final String name;
    private final String email;

    /**
     * Gets assistant's email
     * @return assistant email
     */
    public String getEmail() { return email; }

    /**
     * Assistant constructor
     * @param name Assistant name
     * @param email Assistant email
     */
    public Assistant(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() { return "| %s | %s |".formatted(name, email); }
}
