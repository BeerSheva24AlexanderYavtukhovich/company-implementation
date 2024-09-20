package telran.employees;

public class Employee {
    private long id;
    private int basicSalary;
    private String department;

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
}
