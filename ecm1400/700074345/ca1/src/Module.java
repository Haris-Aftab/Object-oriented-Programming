import java.util.Arrays;

public class Module {
	private int year;
	private byte term;
	private ModuleDescriptor module;
	private StudentRecord[] records;
	private double finalAverageGrade;

	public int getYear() { return year; }
	public byte getTerm() { return term; }
	public ModuleDescriptor getModule() { return module; }
	public StudentRecord[] getRecords() { return records; }
	public double getFinalAverageGrade() { return finalAverageGrade; }

	/**
	 * Sets the array of student records for each student that takes this module
	 * @param records array of student records for each student that takes this module
	 */
	public void setRecords(StudentRecord[] records) {
		for (int i=0; i<records.length; i++){
			for (int j=0; j<records.length; j++){
				if (records[i].equals(records[j]) && i != j){
					System.err.println("""
							Error has occurred.\s
							CheckList:
							. A module descriptor can only be offered once per year and term""");
					System.exit(1);
				}
			}
		}
		this.records = records;
		setFinalAverageGrade();
	}

	/**
	 * The final average grade is the mean of the student record final scores for this module
	 */
	public void setFinalAverageGrade() {
		for (StudentRecord record : records) finalAverageGrade += record.getFinalScore();
		finalAverageGrade /= records.length;
	}

	/**
	 * Module constructor which sets the initial information about the module
	 * @param year The Year the module is taken in
	 * @param term The term the module is taken in
	 * @param module The module description
	 */
	public Module(int year, byte term, ModuleDescriptor module){
		this.year = year;
		this.term = term;
		this.module = module;
	}

	@Override
	public String toString() {
		return "Module{" +
				"year=" + year +
				", term=" + term +
				", module=" + module +
				", records=" + java.util.Arrays.toString(records) +
				", finalAverageGrade=" + finalAverageGrade +
				'}';
	}
}
