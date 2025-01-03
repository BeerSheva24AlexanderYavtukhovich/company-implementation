package telran.employees.db;

import java.util.List;

import telran.employees.Employee;

public interface CompanyRepository {
    List<Employee> getEmployees();
}
