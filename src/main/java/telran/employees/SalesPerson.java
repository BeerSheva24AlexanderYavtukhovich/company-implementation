package telran.employees;

import org.json.JSONObject;

public class SalesPerson extends WageEmployee {
    float percent;
    long sales;

    public SalesPerson() {

    }

    public SalesPerson(long id, int basicSalary, String department, int wage, int hours, float percent, long sales) {
        super(id, basicSalary, department, wage, hours);
        this.percent = percent;
        this.sales = sales;
    }

    @Override
    public int computeSalary() {
        return (int) (super.computeSalary() + sales * percent / 100);
    }

    @Override
    protected void fillJSON(JSONObject jsonObj) {
        super.fillJSON(jsonObj);
        jsonObj.put("percent", percent);
        jsonObj.put("sales", sales);
    }

    @Override
    protected void setObject(JSONObject jsonObj) {
        super.setObject(jsonObj);
        percent = jsonObj.getFloat("percent");
        sales = jsonObj.getLong("sales");
    }
}
