import java.util.Arrays;

public class StudentRecord {
	private Student student;
	private Module module;
	private double[] marks;
	private double finalScore;
	private Boolean isAboveAverage;

	public Student getStudent() { return student; }
	public Module getModule() { return module; }
	public double[] getMarks() { return marks; }
	public double getFinalScore() { return finalScore; }
	public Boolean getAboveAverage() { return isAboveAverage; }

	/**
	 * Sets the final score the student achieved in a particular module
	 */
	public void setFinalScore() {
		for (int i=0; i<marks.length; i++){
			finalScore += marks[i] * getModule().getModule().getContinuousAssignmentWeights()[i];
		}
	}

	/**
	 * Calculates if the student is above average in a particular module
	 */
	public void setAboveAverage() {
		isAboveAverage = finalScore > module.getFinalAverageGrade();
	}

	/**
	 * Student constructor which sets the initial information about the student record
	 * @param student A student
	 * @param module A particular module the student takes
	 * @param marks The marks the student got for each assessment in that module
	 */
	public StudentRecord(Student student, Module module, double[] marks){
		for (double mark : marks)
			if (!(mark >= 0 && mark <= 10)) {
				System.err.println("""
						Error has occurred.\s
						CheckList:
						 . Marks must range between 0 and 10""");
				System.exit(1);
			}
		this.student = student;
		this.module = module;
		this.marks = marks;
		setFinalScore();
		}

	@Override
	public String toString() {
		return "StudentRecord{" +
				"student=" + student.getName() +
				", module=" + module.getModule().getName() +
				", marks=" + java.util.Arrays.toString(marks) +
				", finalScore=" + finalScore +
				", isAboveAverage=" + isAboveAverage +
				'}';
	}
}
