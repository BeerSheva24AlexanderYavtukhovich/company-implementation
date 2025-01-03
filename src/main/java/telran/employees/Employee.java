package telran.employees;

import org.json.JSONObject;

public class Employee {
    private long id;
    private int basicSalary;
    private String department;

    public Employee() {

    }

    @SuppressWarnings("unchecked")
    public static Employee getEmployeeFromJSON(String jsonStr) {
        JSONObject jsonObj = new JSONObject(jsonStr);
        String className = jsonObj.getString("className");
        try {
            Class<Employee> clazz = (Class<Employee>) Class.forName(className);
            Employee empl = clazz.getConstructor().newInstance();
            empl.setObject(jsonObj);
            return empl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected void setObject(JSONObject jsonObj) {
        id = jsonObj.getLong("id");
        basicSalary = jsonObj.getInt("basicSalary");
        department = jsonObj.getString("department");
    }

    public Employee(long id, int basicSalary, String department) {
        this.id = id;
        this.basicSalary = basicSalary;
        this.department = department;
    }

    public int computeSalary() {
        return basicSalary;
    }

    public long getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null && getClass() == obj.getClass()) {
            Employee empl = (Employee) obj;
            isEqual = this.getId() == empl.getId();
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    @Override
    public String toString() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("className", getClass().getName());
        fillJSON(jsonObj);
        return jsonObj.toString();
    }

    protected void fillJSON(JSONObject jsonObj) {
        jsonObj.put("id", id);
        jsonObj.put("basicSalary", basicSalary);
        jsonObj.put("department", department);
    }

    public int getBasicSalary() {
        return basicSalary;
    }
}
