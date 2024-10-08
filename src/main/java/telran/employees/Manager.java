package telran.employees;

import org.json.JSONObject;

public class Manager extends Employee {
    private float factor;

    public Manager() {

    }

    public Manager(long id, int basicSalary, String department, float factor) {
        super(id, basicSalary, department);
        this.factor = factor;
    }

    @Override
    public int computeSalary() {
        return (int) (super.computeSalary() * factor);
    }

    public float getFactor() {
        return factor;
    }

    @Override
    protected void fillJSON(JSONObject jsonObj) {
        super.fillJSON(jsonObj);
        jsonObj.put("factor", factor);
    }

    @Override
    protected void setObject(JSONObject jsonObj) {
        super.setObject(jsonObj);
        factor = jsonObj.getFloat("factor");
    }
}
