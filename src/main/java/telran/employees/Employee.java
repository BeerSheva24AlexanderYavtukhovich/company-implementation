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
        // TODO
        // according to id only
        throw new UnsupportedOperationException();
    }
}
