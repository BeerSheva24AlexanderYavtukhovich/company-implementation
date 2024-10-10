package telran.employees;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import telran.io.Persistable;

public class CompanyImpl implements Company, Persistable {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();

    private class CompanyIterator implements Iterator<Employee> {
        private final Iterator<Employee> iterator = employees.values().iterator();
        private Employee lastEmployee;

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
            iterator.remove();
            removeEmployeeFromLists(lastEmployee);
            lastEmployee = null;
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyIterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        if (employees.containsKey(empl.getId())) {
            throw new IllegalStateException();
        }
        employees.put(empl.getId(), empl);
        String department = empl.getDepartment();
        employeesDepartment.computeIfAbsent(department, k -> new ArrayList<>()).add(empl);
        if (empl instanceof Manager manager) {
            managersFactor.computeIfAbsent(manager.getFactor(), k -> new LinkedList<>()).add(manager);
        }
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
        Employee removedEmployee = employees.remove(id);
        removeEmployeeFromLists(removedEmployee);
        return removedEmployee;
    }

    @Override
    public int getDepartmentBudget(String departmentName) {
        int budget = 0;
        List<Employee> employeesInDepartment = employeesDepartment.get(departmentName);
        if (employeesInDepartment != null) {
            budget = employeesInDepartment.stream()
                    .mapToInt(Employee::computeSalary)
                    .sum();
        }
        return budget;
    }

    @Override
    public String[] getDepartments() {
        return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        Entry<Float, List<Manager>> lastEntry = managersFactor.lastEntry();
        Manager[] managers = new Manager[0];
        if (lastEntry != null) {
            managers = lastEntry.getValue().stream().toArray(Manager[]::new);
        }
        return managers;
    }

    private void removeEmployeeFromLists(Employee empl) {
        removeEmployeeFromDepartment(empl);
        if (empl instanceof Manager manager) {
            removeManagerFromManagersFactor(manager);
        }
    }

    private void removeEmployeeFromDepartment(Employee empl) {
        String department = empl.getDepartment();
        List<Employee> departmentEmployees = employeesDepartment.get(department);
        departmentEmployees.remove(empl);
        if (departmentEmployees.isEmpty()) {
            employeesDepartment.remove(department);
        }
    }

    private void removeManagerFromManagersFactor(Manager manager) {
        Float factor = manager.getFactor();
        List<Manager> listManagers = managersFactor.get(factor);
        if (listManagers != null) {
            listManagers.remove(manager);
            if (listManagers.isEmpty()) {
                managersFactor.remove(factor);
            }
        }
    }

    @Override
    public void saveTofile(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            forEach(writer::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void restoreFromFile(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            reader.lines().map(Employee::getEmployeeFromJSON).forEach(this::addEmployee);
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}