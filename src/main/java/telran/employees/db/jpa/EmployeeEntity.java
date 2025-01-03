package telran.employees.db.jpa;

import org.json.JSONObject;
import jakarta.persistence.*;
import telran.employees.*;

@Table(name = "employees")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EmployeeEntity {
    @Id
    private long id;
    @Column(name = "basic_salary")
    private int basicSalary;
    private String department;

    protected void fromEmployeeDto(Employee empl) {
        id = empl.getId();
        basicSalary = empl.getBasicSalary();
        department = empl.getDepartment();
    }

    protected void toJsonObject(JSONObject jsonObject) {
        jsonObject.put("id", id);
        jsonObject.put("basicSalary", basicSalary);
        jsonObject.put("department", department);
    }
}
