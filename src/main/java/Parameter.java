/**
 * Created by sami on 23/06/16.
 * @author sami
 */
public class Parameter {

    /**
     * Parameter name
     */

    private String Name;

    /**
     * Parameter value
     */

    private String Value;

    /**
     * Paramater date
     */

    private String  Date;

    /**
     * Constructor
     */

    public Parameter(String name)
    {
        Name=name;
    }

    /**
     * Constructor
     */

    public Parameter(String name, String value, String date) {
        setName(name);
        setValue(value);
        setDate(date);
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

    public String getValue() {
        return Value;
    }

    /**
     * Setter
     */

    public void setValue(String value) {
        Value = value;
    }

    /**
     * Getter
     */

    public String getName() {
        return Name;
    }

    /**
     * Setter
     */

    public void setName(String name) {
        Name = name;
    }
}
