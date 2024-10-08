package telran.employees;

import org.json.JSONObject;

public class WageEmployee extends Employee {
    private int wage;
    private int hours;

    public WageEmployee() {

    }

    public WageEmployee(long id, int basicSalary, String department, int wage, int hours) {
        super(id, basicSalary, department);
        this.wage = wage;
        this.hours = hours;
    }

    @Override
    public int computeSalary() {
        return super.computeSalary() + wage * hours;
    }

    @Override
    protected void fillJSON(JSONObject jsonObj) {
        super.fillJSON(jsonObj);
        jsonObj.put("wage", wage);
        jsonObj.put("hours", hours);
    }

    @Override
    protected void setObject(JSONObject jsonObj) {
        super.setObject(jsonObj);
        wage = jsonObj.getInt("wage");
        hours = jsonObj.getInt("hours");
    }
}
