import java.util.Arrays;

public class Student {
	
	private int id;
	private String name;
	private char gender;
	private double gpa;
	private StudentRecord[] records;

	public int getId() { return id; }
	public String getName() { return name; }
	public char getGender() { return gender; }
	public double getGpa() { return gpa; }
	public StudentRecord[] getRecords() { return records; }


	/**
	 * Sets GPA for student
	 */
	public void setGpa() {
		for (StudentRecord record : records) {
			gpa += record.getFinalScore();
		}
		gpa /= records.length;
	}

	/**
	 * Sets the array of student records for each module the student takes
	 * @param records Array of student records for each module the student takes
	 */
	public void setRecords(StudentRecord[] records) {
		for (int i=0; i<records.length; i++){
			for (int j=0; j<records.length; j++){
				if (records[i].equals(records[j]) && i != j){
					System.err.println("""
							Error has occurred.\s
							CheckList:
							. A student can only have one record per module""");
					System.exit(1);
				}
			}
		}
		this.records = records;
		orderRecords();
		setGpa();
	}

	/**
	 * Orders the student records by year and term
	 */
	public void orderRecords() {
		for (int i=0; i<records.length-1; i++){
			if ((records[i].getModule().getYear() > records[i+1].getModule().getYear()) || ((records[i].getModule().getYear() == records[i+1].getModule().getYear()) && records[i].getModule().getTerm() > records[i+1].getModule().getTerm())) {
				StudentRecord temp = records[i];
				records[i] = records[i + 1];
				records[i + 1] = temp;
			}
		}
	}

	/**
	 * This generates a transcript containing all student records, grouped by year and term.
	 * @return This returns the transcript as a string
	 */
	public String printTranscript() {
		final int RL = records.length;
		String[] studentDetails = new String[RL];
		StringBuilder studentRecord = new StringBuilder();

		for (int i = 0; i < RL-1; i++) {
			if ((records[i].getModule().getYear() != records[i+1].getModule().getYear()) || ((records[i].getModule().getYear() == records[i+1].getModule().getYear()) && (records[i].getModule().getTerm() != records[i+1].getModule().getTerm()))) {
				studentDetails[i] = "| " + records[i].getModule().getYear() + " | " + records[i].getModule().getTerm() +
						" | " + records[i].getModule().getModule().getCode() + " | " + records[i].getFinalScore() + " |\n\n";
			} else {studentDetails[i] = "| " + records[i].getModule().getYear() + " | " + records[i].getModule().getTerm() +
					" | " + records[i].getModule().getModule().getCode() + " | " + records[i].getFinalScore() + " |\n";}
		}
		studentDetails[RL-1] = "| " + records[RL-1].getModule().getYear() + " | " + records[RL-1].getModule().getTerm() +
				" | " + records[RL-1].getModule().getModule().getCode() + " | " + records[RL-1].getFinalScore() + " |";
		for (String studentDetail : studentDetails) { studentRecord.append(studentDetail); }

		return "University of Knowledge - Official Transcript\n\n \nID: %d\nName: %s\nGPA: %s\n\n%s".formatted(id, name, gpa, studentRecord);
	}

	/**
	 * Student constructor which sets the initial information about the student
	 * @param id Student id
	 * @param name Student name
	 * @param gender Student gender
	 */
	public Student(int id, String name, char gender){
		if ("MFX".contains(Character.toString(gender)) && id > 0  && !name.isEmpty()){
			this.id = id;
			this.name = name;
			this.gender = gender;
		} else {
			System.err.println("Error has occurred. \n" +
							   "CheckList:\n" +
							   ". ID and name cannot be null\n" +
							   ". ID must be unique\n" +
							   ". Gender must be 'M', 'F', 'X' or empty");
			System.exit(1);
		}
	}

	/**
	 * Student constructor which sets the initial information about the student (without gender)
	 * @param id Student id
	 * @param name Student name
	 */
	public Student(int id, String name){
		if (id > 0 && !name.isEmpty()){
			this.id = id;
			this.name = name;
		} else {
			System.err.println("Error has occurred. \n" +
							   "CheckList:\n" +
							   " . ID and name cannot be null\n" +
							   " . ID must be unique");
			System.exit(1);
		}
	}


	//@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", name='" + name + '\'' +
				", gender=" + gender +
				", gpa=" + gpa +
				", records=" + Arrays.toString(getRecords()) +
				'}';
	}
}
