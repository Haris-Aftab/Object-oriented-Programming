/**
 * CA2.
 * @author Haris Aftab
 */

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class which takes in user input to user this program.
 */
public class BookingApp {
    public BookingSystem bookingSystem = new BookingSystem();
    public UniversityResources universityResources = new UniversityResources();
    public ArrayList<String> roomsAdded = new ArrayList<>();
    public ArrayList<String> assistantsAdded = new ArrayList<>();


    /**
     * Shows options that user can do.
     * Goes to the function that the user inputs.
     */
    public void mainMenu() {
        // repeats until user enters valid response.
        while (true) {
            clearScreen();
            System.out.println("""
                    University of Knowledge - COVID test

                    Manage Bookings
                                    
                    Please, enter the number to select your option:

                    To manage Bookable Rooms:
                        1. List
                        2. Add
                        3. Remove
                    To manage Assistants on Shift:
                        4. List
                        5. Add
                        6. Remove
                    To manage Bookings:
                        7. List
                        8. Add
                        9. Remove
                        10. Conclude                                
                    After selecting one the options above, you will be presented other screens.
                    If you press 0, you will be able to return to this main menu.
                    Press -1 (or ctrl+c) to quit this application.
                    """);

            try {
                Scanner input = new Scanner(System.in);

                int option = input.nextByte();
                clearScreen(); // clears the screen
                // this take the user to the function they input.
                switch (option) {
                    case -1 -> System.exit(0);
                    case 0 -> mainMenu();
                    case 1, 4 -> ListBookableRoomsOrListAssistantsOnShifts(option);
                    case 2 -> addBookableRooms(universityResources.getRooms(), universityResources.showRooms());
                    case 3 -> removeBookableRooms();
                    case 5 -> addAssistantOnShift(universityResources.getAssistants(), universityResources.showAssistants());
                    case 6 -> removeAssistantOnShift();
                    case 7 -> listBookings();
                    case 8 -> addBooking();
                    case 9 -> removeBooking();
                    case 10 -> concludeBooking();
                }
            } catch (Exception e) {System.out.println("\nError! \nEnter a valid sequential ID!\n");}
        }
    }

    /**
     * Lists bookable rooms or lists assistants on shift.
     * @param choice This decides whether to show bookable rooms or assistants on shift.
     */
    public void ListBookableRoomsOrListAssistantsOnShifts(int choice) {
        String list;
        if (choice == 1) list = bookingSystem.showBookableRoom();
        else list = bookingSystem.showAssistantOnShift();

        System.out.println("University of Knowledge - COVID test\n\n" +
                list +
                "\n0. Back to main menu. \n-1. Quit application.\n");

        // repeats until user enters valid response.
        while (true) {
            try {
                Scanner input = new Scanner(System.in);
                byte option = input.nextByte();
                // check if user wants to leave this screen
                if (option == 0) {
                    mainMenu();
                    break;
                } else if (option == -1) System.exit(0);
                System.out.println("\nEnter either 0 or -1!\n");
            } catch (Exception e) { System.out.println("\nEnter either 0 or -1!\n");}
        }
    }

    /**
     * Adds bookable rooms
     * @param rooms List of rooms the uni has.
     * @param roomsList shows the rooms in a list format.
     */
    public void addBookableRooms(Room[] rooms, String roomsList) {
        Scanner input = new Scanner(System.in);

        System.out.println("University of Knowledge - COVID test\n\n" +
                "Adding bookable room\n\n" +
                roomsList);

        // repeats until user enters valid response.
        while (true) {
            System.out.println("""
                    Please, enter one of the following:\s

                    The sequential ID listed to a room, a date (dd/mm/yyyy), and a time (HH:MM),
                    separated by a white space.
                    0. Back to main menu.
                    -1. Quit application.
                    """);

            String addRoom = input.nextLine();

            // check if user wants to leave this screen
            if (addRoom.equals("0") || addRoom.equals("-1")) {
                if (addRoom.equals("0")) {
                    mainMenu();
                    break;
                }
                System.exit(0);
            }


            try {
                if (addRoom.length() == 19 && !roomsAdded.contains(addRoom)) { // checks if room has not already been added
                    // this splits the sequential ID and the date
                    int sequentialID = Integer.parseInt(addRoom.substring(0, 2)) - 11;
                    // checks format of date and time
                    String inputDate = addRoom.substring(3, 13);
                    String time = addRoom.substring(14, 19);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    sdf.setLenient(false);
                    Date date = sdf.parse(inputDate+" "+time); // change date type to date

                    // checks if all the user input is valid
                    if (sequentialID >= 0 && sequentialID < rooms.length && time.matches("\\d{2}:\\d{2}") && time.startsWith("0") && "789".contains(time.substring(1, 2)) && time.startsWith("00", 3)) {
                        roomsAdded.add(addRoom); // adds to this array list so the function knows the bookable room has been added already
                        bookingSystem.addBookableRoom(rooms[sequentialID], date); // adds bookable room to the system
                        System.out.println("\nBookable Room added successfully: \n" +
                                bookingSystem.getBookableRooms().get(bookingSystem.getBookableRooms().size() - 1));
                    } else {
                        System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
                    }
                } else if (roomsAdded.contains(addRoom)) {
                    System.out.println("\nError! \nRoom already added! \n");
                } else {
                    System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
                }
            } catch (ParseException e) {
                System.out.println("\nError! \nEnter date in the correct format (dd/mm/yyyy)");
            } catch (Exception e) {
                System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
            }
        }
    }

    /**
     * Removes bookable room
     */
    public void removeBookableRooms() {
        HashMap<Byte, BookableRoom> bookableRooms = new HashMap<>(); // links sequential id to bookable room

        System.out.println("University of Knowledge - COVID test \n\nList of empty bookable rooms: ");
        byte sequentialID = 10;
        // lists empty bookable rooms
        for (BookableRoom bookableRoom : bookingSystem.getBookableRooms())
            if (bookableRoom.getStatus() == RoomStatus.EMPTY) {
                sequentialID++;
                System.out.printf("%d. %s%n", sequentialID, bookableRoom);
                bookableRooms.put(sequentialID, bookableRoom);
            }
        System.out.println("\nRemoving bookable room\n");

        while (true) {
            System.out.println("""
                    Please, enter one of the following:\s

                    The sequential ID to select the bookable room to be removed.
                    0. Back to main menu.
                    -1. Quit application.
                    """);

            try {
                Scanner input = new Scanner(System.in);
                byte choice = input.nextByte();
                // check if user wants to leave this screen
                if (choice == 0) {
                    mainMenu();
                    break;
                } else if (choice == -1) {
                    System.exit(0);
                } else if (choice > 10 && choice < bookableRooms.size()+11 && bookableRooms.containsKey(choice)) { // checks if user input is valid
                    System.out.println("\nBookable Room removed successfully: \n" +
                            bookableRooms.get(choice));
                    roomsAdded.remove(bookableRooms.get(choice)); // removes room from array list
                    bookingSystem.removeBookableRoom(bookableRooms.get(choice)); // removes room from system
                    bookableRooms.remove(choice);
                    continue;
                } System.out.println("\nError! \nEnter a valid sequential ID!");
            } catch (Exception e) {System.out.println("\nError! \nChoose a valid sequential ID!\n"); }
        }
    }

    /**
     * Adds assistant to shift
     * @param assistants list of assistants the uni has
     * @param assistantList list of assistants in list format
     */
    public void addAssistantOnShift(Assistant[] assistants, String assistantList) {
        Scanner input = new Scanner(System.in);

        System.out.println("University of Knowledge - COVID test\n\n" +
                "Adding assistant on shift\n\n" +
                assistantList);

        while (true) {
            System.out.println("""
                    Please, enter one of the following:\s

                    The sequential ID of an assistant and date (dd/mm/yyyy), separated by a white space.
                    0. Back to main menu.
                    -1. Quit application.
                    """);

            String addAssistant = input.nextLine();
            // check is user wants to leave this screen
            if (addAssistant.equals("0") || addAssistant.equals("-1")) {
                if (addAssistant.equals("0")) {
                    mainMenu();
                    break;
                }
                System.exit(0);
            }

            try{
                if (addAssistant.length() == 13 && !assistantsAdded.contains(addAssistant)) { // checks if assistant has not already been added
                    // this splits the sequential ID and the date
                    int sequentialID = Integer.parseInt(addAssistant.substring(0, 2)) - 11;
                    // checks format of date
                    String inputDate = addAssistant.substring(3, 13);
                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false);
                    Date date = sdf.parse(inputDate);
                    // checks if the user input is valid
                    if (sequentialID >= 0 && sequentialID < assistants.length) {
                        assistantsAdded.add(addAssistant); // adds to this array list so the function knows the assistant has been added already
                        bookingSystem.addAssistantOnShift(assistants[sequentialID], inputDate); // adds assistant to the system
                        System.out.println("Assistant on Shift added successfully: \n" +
                        bookingSystem.getAssistantOnShifts().get(bookingSystem.getAssistantOnShifts().size()-3)+"\n"+
                        bookingSystem.getAssistantOnShifts().get(bookingSystem.getAssistantOnShifts().size()-2)+"\n"+
                        bookingSystem.getAssistantOnShifts().get(bookingSystem.getAssistantOnShifts().size()-1));
                    } else {
                        System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
                    }
                } else if (assistantsAdded.contains(addAssistant)) {
                    System.out.println("\nError! \nAssistant already added! \n");
                } else {
                    System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
                }
            } catch (ParseException e) {
                System.out.println("\nError! \nEnter date in the correct format (dd/mm/yyyy)");
            } catch (Exception e) {
                System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
            }
        }
    }

    /**
     * Removes assistant on shift
     */
    public void removeAssistantOnShift () {
        HashMap<Byte, AssistantOnShift> assistantOnShifts = new HashMap<>(); // links sequential id to assistant on shift

        System.out.println("University of Knowledge - COVID test \n\nList of free assistants on shifts: ");
        byte sequentialID = 10;
        // lists free assistant on shift
        for (AssistantOnShift assistantOnShift : bookingSystem.getAssistantOnShifts())
            if (assistantOnShift.getStatus() == AssistantStatus.FREE) {
                sequentialID++;
                System.out.printf("%d. %s%n", sequentialID, assistantOnShift);
                assistantOnShifts.put(sequentialID, assistantOnShift);
            }
        System.out.println("\nRemoving assistant on shift \n");

        while (true) {
            System.out.println("""
                    Please, enter one of the following:\s

                    The sequential ID to select the assistant on shift to be removed.
                    0. Back to main menu.
                    -1. Quit application.
                    """);

            try {
                Scanner input = new Scanner(System.in);
                byte choice = input.nextByte();
                // check if user wants to leave this screen
                if (choice == 0) {
                    mainMenu();
                    break;
                } else if (choice == -1) {
                    System.exit(0);
                } else if (choice > 10 && choice < assistantOnShifts.size()+11 && assistantOnShifts.containsKey(choice)) { // checks if user input is valid
                    System.out.println("Assistant on Shift removed successfully: \n" +
                            assistantOnShifts.get(choice));
                    assistantsAdded.remove(assistantOnShifts.get(choice)); // removes assistant on shift from array list
                    bookingSystem.removeAssistantOnShift(assistantOnShifts.get(choice)); // removes assistant on shift from the system
                    assistantOnShifts.remove(choice);
                    continue;
                } System.out.println("\nError! \nEnter a valid sequential ID!");
            } catch (Exception e) {System.out.println("\nError! \nChoose a valid sequential ID!\n"); }
        }
    }

    /**
     * Shows the bookings made
     */
    public void listBookings() {
        System.out.println("""
                University of Knowledge - COVID test

                Select which booking to list:
                1. All
                2. Only bookings status:SCHEDULED
                3. Only bookings status:COMPLETED
                0. Back to main menu.
                -1. Quit application.
                """);

        try {
            Scanner input = new Scanner(System.in);
            byte option = input.nextByte();
            if (option >= -1 && option < 4) {
                // lists chosen bookings or leaves screen
                switch (option) {
                    case -1 -> System.exit(0);
                    case 0 -> mainMenu();
                    case 2 -> {
                        System.out.println("List of bookings status:SCHEDULED: ");
                        for (Booking booking : bookingSystem.getBookings())
                            if (booking.getStatus() == BookingStatus.SCHEDULED) System.out.println(booking);
                    }
                    case 3 -> {
                        System.out.println("List of bookings status:COMPLETED: ");
                        for (Booking booking : bookingSystem.getBookings())
                            if (booking.getStatus() == BookingStatus.COMPLETED) System.out.println(booking);
                    }
                    default -> System.out.println(bookingSystem.showBooking());
                }
            } else System.out.println(bookingSystem.showBooking());
        } catch (Exception e) {
            System.out.println(bookingSystem.showBooking());
        }

        System.out.println("\n0. Back to main menu. \n-1. Quit application.\n");
        while (true) {
            try {
                Scanner input1 = new Scanner(System.in);
                byte option1 = input1.nextByte();
                if (option1 == 0) {
                    mainMenu();
                    break;
                } else if (option1 == -1) System.exit(0);
                System.out.println("\nEnter either 0 or -1!\n");
            } catch (Exception e) { System.out.println("\nEnter either 0 or -1!\n"); }
        }
    }

    /**
     * Adds booking to the system
     */
    public void addBooking() {
        System.out.println("""
                University of Knowledge - COVID test

                Adding booking (appointment for a COVID test) to the system

                List of available time-slots:""");

        int sequentialID = 10;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        ArrayList<Date> availableDates = new ArrayList<>();

        // if there is a bookable room not full and an assistant on shift that is free,
        // both with the same date, the date is outputted in a list for the user to choose
        if (bookingSystem.roomAvailable() && bookingSystem.assistantAvailable()) {
            for (BookableRoom bookableRoom : bookingSystem.getBookableRooms()) {
                if (bookableRoom.getStatus() != RoomStatus.FULL) {
                    for (AssistantOnShift assistantOnShift : bookingSystem.getAssistantOnShifts()) {
                        if (assistantOnShift.getStatus() == AssistantStatus.FREE && bookableRoom.getDate().equals(assistantOnShift.getDate())) {
                            if (!availableDates.contains(bookableRoom.getDate())) {
                                sequentialID++;
                                availableDates.add(bookableRoom.getDate());
                                System.out.printf("%d. %s\n", sequentialID, formatter.format(bookableRoom.getDate()));
                            }
                        }
                    }
                }
            }
        }

        Scanner input = new Scanner(System.in);


            while (true) {
                System.out.println("""
                        Please, enter one of the following:

                        The sequential ID of an available time-slot and the student email, separated by a white space.
                        0. Back to main menu.
                        -1. Quit application.
                        """);

                String bookingSlot = input.nextLine(); // users chosen time slot

                if (bookingSlot.equals("0") || bookingSlot.equals("-1")) {
                    if (bookingSlot.equals("0")) {
                        mainMenu();
                        break;
                    }
                    System.exit(0);
                }
                if (availableDates.size() > 0) {
                    try {
                        int sequentialIDChosen = Integer.parseInt(bookingSlot.substring(0, 2)) - 11;

                        // checks users email is in valid format
                        String email = bookingSlot.substring(3);
                        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@uok.ac.uk");
                        Matcher matcher = pattern.matcher(email);

                        // if user input is valid
                        if (sequentialIDChosen >= 0 && sequentialIDChosen < availableDates.size() && matcher.matches()) {
                            // hashmap of one set of a bookable room and an assistant on shift
                            HashMap bookingPair = getPair(availableDates.get(sequentialIDChosen));
                            // adds booking to the system
                            bookingSystem.addBooking((BookableRoom) bookingPair.get(bookingPair.keySet().toArray()[0]), (AssistantOnShift) bookingPair.keySet().toArray()[0], availableDates.get(sequentialIDChosen), email);
                            System.out.println("Booking added successfully:\n" +
                                    bookingSystem.getBookings().get(bookingSystem.getBookings().size() - 1));
                        } else { System.out.println("\nError! \nEnter the correct details in the correct format! \n "); }
                    } catch (Exception e) {
                        System.out.println("\nError! \nEnter the correct details in the correct format! \n ");
                    }
                } else System.out.println("\nError! \nEnter the correct details in the correct format! \n ");


            }

    }

    /**
     * This finds a non full bookable room and a free assistant on shift and pairs them together
     * @param date The date the user has chosen to add a booking
     * @return bookable room and assistant pair
     */
    public HashMap getPair(Date date) {
        HashMap<AssistantOnShift, BookableRoom> bookingPair = new HashMap<>();
        if (bookingSystem.roomAvailable() && bookingSystem.assistantAvailable()) {
            for (BookableRoom bookableRoom : bookingSystem.getBookableRooms()) {
                if (bookableRoom.getStatus() != RoomStatus.FULL && bookableRoom.getDate().equals(date)) {
                    for (AssistantOnShift assistantOnShift : bookingSystem.getAssistantOnShifts()) {
                        if (assistantOnShift.getStatus() == AssistantStatus.FREE && assistantOnShift.getDate().equals(date)) {
                            bookingPair.put(assistantOnShift, bookableRoom);
                            return bookingPair;
                        }
                    }
                }
            }
        }
        return bookingPair;
    }

    /**
     * Removes booking from system
     */
    public void removeBooking() {
        HashMap<Byte, Booking> bookingHashMap = new HashMap<>(); // maps sequential ID and booking together

        System.out.println("University of Knowledge - COVID test\n\n " +
                "List of  booking status:SCHEDULED: ");
        byte sequentialID = 10;
        // lists scheduled bookings
        for (Booking booking : bookingSystem.getBookings()) {
            if (booking.getStatus().equals(BookingStatus.SCHEDULED)) {
                sequentialID++;
                System.out.printf("%d. %s%n", sequentialID, booking);
                bookingHashMap.put(sequentialID, booking);
            }
        }
        System.out.println("\nRemoving booking from the system \n");

        while (true) {
           System.out.println("""                                      
                    Please, enter one of the following:\s

                    The sequential ID to select the assistant on shift to be removed.
                    0. Back to main menu.
                    -1. Quit application.
                    """);
           try {
               Scanner input = new Scanner(System.in);
               byte choice = input.nextByte();
               // checks if user wants to leave the screen
               if (choice == 0) {
                   mainMenu();
                   break;
               } else if (choice == -1) {
                   System.exit(0);
               } else if (choice > 10 && choice < bookingHashMap.size()+11) { // checks if user input is valid
                   System.out.println("Booking removed successfully: \n" +
                           bookingHashMap.get(choice));
                   bookingSystem.removeBooking(bookingHashMap.get(choice)); // removes booking from system
                   continue;
               } System.out.println("\nError! \nEnter a valid sequential ID!");
           } catch (Exception e) {
               System.out.println("\nError! \nEnter a valid sequential ID!");
           }
       }
    }

    /**
     * This method complete a booking
     */
    public void concludeBooking() {
        HashMap<Byte, Booking> bookingHashMap = new HashMap<>();

        System.out.println("University of Knowledge - COVID test \n\nList of booking status:SCHEDULED: ");
        byte sequentialID = 10;
        // lists scheduled bookings
        for (Booking booking : bookingSystem.getBookings()) {
            if (booking.getStatus().equals(BookingStatus.SCHEDULED)) {
                sequentialID++;
                System.out.printf("%d. %s%n", sequentialID, booking);
                bookingHashMap.put(sequentialID, booking);
            }
        }
        System.out.println("Conclude booking\n");

        while (true) {
            System.out.println("""                                      
                    Please, enter one of the following:\s

                    The sequential ID to select the assistant on shift to be removed.
                    0. Back to main menu.
                    -1. Quit application.
                    """);

            try {
                Scanner input = new Scanner(System.in);
                byte choice = input.nextByte();
                // checks if user wants to leave the screen
                if (choice == 0) {
                    mainMenu();
                    break;
                } else if (choice == -1) {
                    System.exit(0);
                } else if (choice > 10 && choice < bookingHashMap.size()+11) { // checks if user input is valid
                    System.out.println("Booking completed successfully: \n" +
                            bookingHashMap.get(choice));
                    // completes booking
                    bookingSystem.getBookings().get(bookingSystem.getBookings().indexOf(bookingHashMap.get(choice))).setStatus(BookingStatus.COMPLETED);
                    continue;
                } System.out.println("\nError! \nEnter a valid sequential ID!");
            } catch (Exception e) {
                System.out.println("\nError! \nEnter a valid sequential ID!");
            }
        }
    }

    /**
     * This clears the screen
     * (This works in my cmd for my Windows device)
     */
    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises initial data
     * Runs mainmenu method
     */
    public static void main(String[] args) {
        BookingApp bookingApp = new BookingApp();

        bookingApp.universityResources.addRooms(); // adds rooms to the uni
        bookingApp.universityResources.addAssistance(); // adds assistants to the uni

        // Initialization
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date1 = sdf.parse("26/02/2021 07:00");
            Date date2 = sdf.parse("26/02/2021 08:00");
            Date date3 = sdf.parse("26/02/2021 09:00");
            Date date4 = sdf.parse("27/02/2021 07:00");
            Date date5 = sdf.parse("27/02/2021 08:00");
            Date date6 = sdf.parse("27/02/2021 09:00");
            Date date7 = sdf.parse("28/02/2021 07:00");
            Date date8 = sdf.parse("28/02/2021 08:00");
            Date date9 = sdf.parse("28/02/2021 09:00");

            bookingApp.roomsAdded.add("11 26/02/2021 07:00");
            bookingApp.roomsAdded.add("11 26/02/2021 08:00");
            bookingApp.roomsAdded.add("11 26/02/2021 09:00");
            bookingApp.roomsAdded.add("11 27/02/2021 07:00");
            bookingApp.roomsAdded.add("11 27/02/2021 08:00");
            bookingApp.roomsAdded.add("11 27/02/2021 09:00");
            bookingApp.roomsAdded.add("11 28/02/2021 07:00");
            bookingApp.roomsAdded.add("11 28/02/2021 08:00");
            bookingApp.roomsAdded.add("11 28/02/2021 09:00");

            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date1);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date2);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date3);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date4);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date5);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date6);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date7);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date8);
            bookingApp.bookingSystem.addBookableRoom(bookingApp.universityResources.getRooms()[0], date9);

            bookingApp.assistantsAdded.add("11 26/02/2021");
            bookingApp.assistantsAdded.add("12 26/02/2021");
            bookingApp.assistantsAdded.add("13 26/02/2021");
            bookingApp.bookingSystem.addAssistantOnShift(bookingApp.universityResources.getAssistants()[0], sdf.format(date1).substring(0,11));
            bookingApp.bookingSystem.addAssistantOnShift(bookingApp.universityResources.getAssistants()[1], sdf.format(date1).substring(0,11));
            bookingApp.bookingSystem.addAssistantOnShift(bookingApp.universityResources.getAssistants()[2], sdf.format(date1).substring(0,11));

            bookingApp.bookingSystem.addBooking(bookingApp.bookingSystem.getBookableRooms().get(0), bookingApp.bookingSystem.getAssistantOnShifts().get(0), date1, "initial1@uok.ac.uk");
            bookingApp.bookingSystem.addBooking(bookingApp.bookingSystem.getBookableRooms().get(0), bookingApp.bookingSystem.getAssistantOnShifts().get(1), date1, "initial1@uok.ac.uk");
            bookingApp.bookingSystem.addBooking(bookingApp.bookingSystem.getBookableRooms().get(1), bookingApp.bookingSystem.getAssistantOnShifts().get(1), date2, "initial2@uok.ac.uk");
            bookingApp.bookingSystem.getBookings().get(1).setStatus(BookingStatus.COMPLETED);



        } catch (ParseException e) {
            e.printStackTrace();
        }


        bookingApp.mainMenu();

    }
}
