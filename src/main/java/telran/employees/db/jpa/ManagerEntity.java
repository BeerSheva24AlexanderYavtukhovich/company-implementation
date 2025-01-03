package telran.employees.db.jpa;

import org.json.JSONObject;

import jakarta.persistence.Entity;
import telran.employees.Employee;
import telran.employees.Manager;

@Entity
public class ManagerEntity extends EmployeeEntity {

    private float factor;

    @Override
    protected void fromEmployeeDto(Employee empl) {
        super.fromEmployeeDto(empl);
        factor = ((Manager) empl).getFactor();
    }

    @Override
    protected void toJsonObject(JSONObject jsonObj) {
        super.toJsonObject(jsonObj);
        jsonObj.put("factor", factor);
    }
}
