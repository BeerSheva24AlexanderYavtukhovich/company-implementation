package telran.employees.db.jpa;

import org.json.JSONObject;

import jakarta.persistence.Entity;
import telran.employees.Employee;
import telran.employees.SalesPerson;

@Entity
public class SalesPersonEntity extends WageEmployeeEntity {
    private float percent;
    private long sales;

    @Override
    protected void fromEmployeeDto(Employee empl) {
        super.fromEmployeeDto(empl);
        percent = ((SalesPerson) empl).getPercent();
        sales = ((SalesPerson) empl).getSales();
    }

    @Override
    protected void toJsonObject(JSONObject jsonObj) {
        super.toJsonObject(jsonObj);
        jsonObj.put("percent", percent);
        jsonObj.put("sales", sales);
    }

}
