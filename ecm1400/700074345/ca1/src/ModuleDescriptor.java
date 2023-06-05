import java.util.ArrayList;
import java.util.Arrays;

public class ModuleDescriptor {
    private String code;
    private String name;
    private double[] continuousAssignmentWeights;

    public String getCode() { return code; }
    public String getName() { return name; }
    public double[] getContinuousAssignmentWeights() { return continuousAssignmentWeights; }

    /**
     * Module descriptor constructor which sets the information about the modules
     * @param code Module descriptor code
     * @param name Module descriptor name
     * @param continuousAssignmentWeights Module descriptor continuous assignment weights
     */
    public ModuleDescriptor(String code, String name, double[] continuousAssignmentWeights) {
        double sum = 0;
        for (double weight:continuousAssignmentWeights){ sum += weight; }
        if (!(code.isEmpty() || name.isEmpty()) && sum == 1) {
            this.code = code;
            this.name = name;
            this.continuousAssignmentWeights = continuousAssignmentWeights;
        } else {
            System.err.println("""
                    Error ha occurred.\s
                    CheckList:
                     . Code and name cannot be null
                     . Code must be unique
                     . Continuous Assessment weights must sum up to 1, and must be non-negative""");
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        return "ModuleDescriptor{" +
               "code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", continuousAssignmentWeights=" + Arrays.toString(continuousAssignmentWeights) +
               '}';
    }
}
