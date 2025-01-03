package telran.employees.db.jpa;

import org.json.JSONObject;

import telran.employees.Employee;

public class EmployeesMapper {
    private static final String ENTITY = "Entity";
    private static final String CLASS_NAME = "className";
    private static final String PACKAGE = "telran.employees.";
    private static final String PACKAGE_ENTITY = PACKAGE + "db.jpa.";

    public static Employee toEmployeeDtoFromEntity(EmployeeEntity entity) {
        String entityClassName = entity.getClass().getSimpleName();
        String dtoClassName = PACKAGE + entityClassName.replaceAll(ENTITY, "");
        JSONObject jsonObject = new JSONObject();
        entity.toJsonObject(jsonObject);
        jsonObject.put(CLASS_NAME, dtoClassName);
        entity.toJsonObject(jsonObject);
        return Employee.getEmployeeFromJSON(jsonObject.toString());
    }

    public static EmployeeEntity toEmployeeEntityFromDto(Employee empl) {
        String dtoClassName = empl.getClass().getSimpleName();
        String entityClassName = PACKAGE_ENTITY + dtoClassName + ENTITY;
        try {
            EmployeeEntity entity = (EmployeeEntity) Class.forName(entityClassName)
                    .getDeclaredConstructor()
                    .newInstance();
            entity.fromEmployeeDto(empl);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
