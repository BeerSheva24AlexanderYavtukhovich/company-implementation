package telran.employees.db.jpa;

import org.json.JSONObject;

import jakarta.persistence.Entity;
import telran.employees.Employee;
import telran.employees.WageEmployee;

@Entity
public class WageEmployeeEntity extends EmployeeEntity {
    private int wage;
    private int hours;

    @Override
    protected void fromEmployeeDto(Employee empl) {
        super.fromEmployeeDto(empl);
        wage = ((WageEmployee) empl).getWage();
        hours = ((WageEmployee) empl).getHours();
    }

    @Override
    protected void toJsonObject(JSONObject jsonObj) {
        super.toJsonObject(jsonObj);
        jsonObj.put("wage", wage);
        jsonObj.put("hours", hours);
    }

}