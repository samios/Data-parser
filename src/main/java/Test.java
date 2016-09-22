
import java.util.ArrayList;

/**
 * Created by sami on 27/06/16.
 *
 * @author sami
 */
public class Test {

    /**
     * Test steps
     */

    private ArrayList<Step> Steps;

    /**
     * Test date
     */

    private String Date;

    /**
     * Test duration
     */

    private String Duration;


    /**
     * Test status
     */

    private String Status;

    /**
     * Failed Step for the test
     */

    private String FailedStep;

    /**
     * Pc on the which the test was done
     */

    private String Pc;

    /**
     * Constructor
     */

    public Test() {
        Steps = new ArrayList<>();
    }

    /**
     * Constructor\t
     */

    /**
     * Constructor
     */

    public Test(ArrayList<Step> steps, String date,String duration, String status, String failedStep, String pc) {
        Steps = steps;
        Date = date;
        Duration=duration;
        Status = status;
        FailedStep = failedStep;
        Pc = pc;
    }

    /**
     * Getter
     */

    public ArrayList<Step> getSteps() {
        return Steps;
    }

    /**
     * Setter
     */

    public void setSteps(ArrayList<Step> steps) {
        Steps = steps;
    }

    /**
     * Getter
     */

    public String getDuration() {
        return Duration;
    }

    /**
     * Setter
     */

    public void setDuration(String duration) {
        Duration = duration;
    }


    /**
     * Getter
     */

    public String getDate() {
        return Date;
    }

    /**
     * Setter
     */

    public void setDate(String date) {
        Date = date;
    }



    /**
     * Getter
     */

    public String getStatus() {
        return Status;
    }

    /**
     * Setter
     */

    public void setStatus(String status) {
        Status = status;
    }

    /**
     * Getter
     */

    public String getFailedMessage() {
        return FailedStep;
    }

    /**
     * Setter
     */

    public void setFailedMessage(String failedStep) {
        FailedStep = failedStep;
    }

    /**
     * Getter
     */

    public String getPc() {
        return Pc;
    }

    /**
     * Setter
     */

    public void setPc(String pc) {
        Pc = pc;
    }

    /**
     * Adds a step to the list of steps
     */

    public void addStep() {
        Steps.add(new Step(null,null,null,null,null,null,null,null));
    }

    /**
     * Adds a step to the list of steps
     */

    public void addStep(String name) {
        Steps.add(new Step(name));
    }


}
