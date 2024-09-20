package telran.employees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class CompanyImpl implements Company {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();

    public class CompanyIterator implements Iterator<Employee> {
        private Iterator<Employee> iterator;
        private Map<String, List<Employee>> employeesDepartment;
        private Employee lastEmployee;

        public CompanyIterator(Iterator<Employee> iterator, Map<String, List<Employee>> employeesDepartment) {
            this.iterator = iterator;
            this.employeesDepartment = employeesDepartment;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Employee next() {
            lastEmployee = iterator.next();
            return lastEmployee;
        }

        @Override
        public void remove() {
            if (lastEmployee == null) {
                throw new IllegalStateException();
            }
            iterator.remove();
            removeEmployeeFromDepartment(lastEmployee);
            lastEmployee = null;
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyIterator(employees.values().iterator(), employeesDepartment);
    }

    @Override
    public void addEmployee(Employee empl) {
        if (employees.containsKey(empl.getId())) {
            throw new IllegalStateException();
        }
        employees.put(empl.getId(), empl);
        String department = empl.getDepartment();
        employeesDepartment.computeIfAbsent(department, k -> new ArrayList<>()).add(empl);
    }

    @Override
    public Employee getEmployee(long id) {
        return employees.get(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        if (!employees.containsKey(id)) {
            throw new NoSuchElementException();
        }
        Employee empl = employees.remove(id);
        removeEmployeeFromDepartment(empl);
        return empl;
    }

    @Override
    public int getDepartmentBudget(String department) {
        HashMap<String, Integer> departmentBudgetsMap = calculateDepartmentBudgets();
        return departmentBudgetsMap.getOrDefault(department, 0);
    }

    private HashMap<String, Integer> calculateDepartmentBudgets() {
        HashMap<String, Integer> budgetsMap = new HashMap<>();
        employeesDepartment.forEach((department, employees) -> {
            int totalBudget = employees.stream()
                    .mapToInt(Employee::computeSalary)
                    .sum();
            budgetsMap.put(department, totalBudget);
        });
        return budgetsMap;
    }

    @Override
    public String[] getDepartments() {
        return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        TreeMap<Float, List<Manager>> factorToManagersMap = new TreeMap<>(Comparator.reverseOrder());

        employees.values().stream()
                .filter(empl -> empl instanceof Manager)
                .map(empl -> (Manager) empl)
                .forEach(manager -> {
                    float factor = manager.getFactor();
                    factorToManagersMap.computeIfAbsent(factor, k -> new ArrayList<>()).add(manager);
                });

        return factorToManagersMap.isEmpty()
                ? new Manager[0]
                : factorToManagersMap.firstEntry().getValue().toArray(new Manager[0]);
    }

    private void removeEmployeeFromDepartment(Employee empl) {
        String department = empl.getDepartment();
        List<Employee> departmentEmployees = employeesDepartment.get(department);
        if (departmentEmployees != null) {
            boolean removed = departmentEmployees.remove(empl);
            if (removed && departmentEmployees.isEmpty()) {
                employeesDepartment.remove(department);
            }
        }
    }
}