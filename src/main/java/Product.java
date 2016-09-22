
import java.util.ArrayList;

/**
 * Created by sami on 20/07/16.
 * @author sami
 */
public class Product {


    /**
     * Client
     */

    private String Client;



    /**
     * Product Family
     */

    private String ProductFamily;

    /**
     * Product serial number
     */

    private String Serial;

    /**
     * Product well
     */

    private String Well;


    /**
     * Product parameters
     */

    private ArrayList<Parameter> Parameters;


    /**
     * Tests for a given product
     */

    private Test BenchTest;

    /**
     * Constructor
     */

    public Product() {
        BenchTest= new Test();
        Parameters = new ArrayList<Parameter>();
    }

    /**
     * Constructor
     */

    public Product(String serial, Test test, ArrayList<Parameter> parameters) {
        Serial = serial;
        BenchTest = test;
        Parameters = parameters;
    }


    /**
     * Setter
     */

    public void setSerial(String serial) {
        Serial = serial;
    }

    /**
     * Setter
     */

    public void setParameters(ArrayList<Parameter> parameters) {
        Parameters = parameters;
    }

    /**
     * Getter
     */


    public ArrayList<Parameter> getParameters() {
        return Parameters;
    }


    /**
     * Getter
     */


    public String getSerial() {
        return Serial;
    }

    /**
     * Getter
     */

    public String getWell() {
        return Well;
    }

    /**
     * Setter
     */

    public void setWell(String well) {
        Well = well;
    }

    /**
     * Getter
     */

    public Test getBenchTest() {
        return BenchTest;
    }

    /**
     * Setter
     */

    public void setBenchTest(Test benchTest) {
        BenchTest = benchTest;
    }

    /**
     * Getter
     */

    public String getClient() {
        return Client;
    }

    /**
     * Setter
     */

    public void setClient(String client) {
        Client = client;
    }

    /**
     * Getter
     */

    public String getProductFamily() {
        return ProductFamily;
    }

    /**
     * Setter
     */

    public void setProductFamily(String productFamily) {
        ProductFamily = productFamily;
    }

    /**
     * Add a parameter to the list of params
     */


    public void addParameter(String name) {

        Parameters.add(new Parameter(name));
    }



    public Step getLastStep()
    {
        ArrayList<Step> steps=getBenchTest().getSteps();
        return steps.get(steps.size());
    }
}


