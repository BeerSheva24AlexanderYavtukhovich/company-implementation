package telran.employees;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.io.Persistable;

class CompanyTest {

	Employee empl1 = new WageEmployee(Constants.ID1, Constants.SALARY1, Constants.DEPARTMENT1, Constants.WAGE1,
			Constants.HOURS1);
	Employee empl2 = new Manager(Constants.ID2, Constants.SALARY2, Constants.DEPARTMENT1, Constants.FACTOR1);
	Employee empl3 = new SalesPerson(Constants.ID3, Constants.SALARY3, Constants.DEPARTMENT2, Constants.WAGE1,
			Constants.HOURS1, Constants.PERCENT1, Constants.SALES1);
	Company company = new CompanyImpl();

	@BeforeEach
	void setCompany() {
		for (Employee empl : new Employee[] { empl1, empl2, empl3 }) {
			company.addEmployee(empl);
		}
	}

	@Test
	void testAddEmployee() {
		Employee empl = new Employee(Constants.ID4, Constants.SALARY1, Constants.DEPARTMENT1);
		company.addEmployee(empl);
		assertThrowsExactly(IllegalStateException.class,
				() -> company.addEmployee(empl));
		assertThrowsExactly(IllegalStateException.class,
				() -> company.addEmployee(empl1));
	}

	@Test
	void testGetEmployee() {
		assertEquals(empl1, company.getEmployee(Constants.ID1));
		assertNull(company.getEmployee(Constants.ID4));
	}

	@Test
	void testRemoveEmployee() {
		assertEquals(empl1, company.removeEmployee(Constants.ID1));
		assertThrowsExactly(NoSuchElementException.class,
				() -> company.removeEmployee(Constants.ID1));
	}

	@Test
	void testGetDepartmentBudget() {
		assertEquals(Constants.SALARY1 + Constants.WAGE1 * Constants.HOURS1 + Constants.SALARY2 * Constants.FACTOR1,
				company.getDepartmentBudget(Constants.DEPARTMENT1));
		assertEquals(
				Constants.SALARY3 + Constants.WAGE1 * Constants.HOURS1 + Constants.PERCENT1 * Constants.SALES1 / 100,
				company.getDepartmentBudget(Constants.DEPARTMENT2));
		assertEquals(0, company.getDepartmentBudget(Constants.DEPARTMENT4));
	}

	@Test
	void testIterator() {
		runTestIterator(company);
	}

	private void runTestIterator(Company companyPar) {
		Employee[] expected = { empl2, empl1, empl3 };
		Iterator<Employee> it = companyPar.iterator();
		int index = 0;
		while (it.hasNext()) {
			assertEquals(expected[index++], it.next());
		}
		assertEquals(expected.length, index);
		assertThrowsExactly(NoSuchElementException.class, it::next);
	}

	@Test
	void testGetDepartments() {
		String[] expected = { Constants.DEPARTMENT1, Constants.DEPARTMENT2 };
		Arrays.sort(expected);
		assertArrayEquals(expected, company.getDepartments());
		expected = new String[] { Constants.DEPARTMENT1 };
		company.removeEmployee(Constants.ID3);
		assertArrayEquals(expected, company.getDepartments());
	}

	@Test
	void testGetManagersWithMostFactor() {
		company.addEmployee(new Manager(Constants.ID4, Constants.SALARY1, Constants.DEPARTMENT1, Constants.FACTOR2));
		Manager[] managersExpected = {
				new Manager(Constants.ID5, Constants.SALARY1, Constants.DEPARTMENT1, Constants.FACTOR3),
				new Manager(Constants.ID6, Constants.SALARY1, Constants.DEPARTMENT1, Constants.FACTOR3),
				new Manager(Constants.ID7, Constants.SALARY1, Constants.DEPARTMENT2, Constants.FACTOR3)
		};
		for (Manager mng : managersExpected) {
			company.addEmployee(mng);
		}
		assertArrayEquals(managersExpected, company.getManagersWithMostFactor());
		company.removeEmployee(Constants.ID4);
		company.removeEmployee(Constants.ID5);
		company.removeEmployee(Constants.ID6);
		company.removeEmployee(Constants.ID7);
		assertArrayEquals(new Manager[] { (Manager) empl2 }, company.getManagersWithMostFactor());
		company.removeEmployee(Constants.ID2);
		assertArrayEquals(new Manager[0], company.getManagersWithMostFactor());

	}

	@Test
	void iteratorRemoveTest() {
		Iterator<Employee> it = company.iterator();
		while (it.hasNext()) {
			Employee empl = it.next();
			if (empl.computeSalary() > 2000) {
				it.remove();
			}
		}
		assertThrowsExactly(IllegalStateException.class, it::remove);
		assertThrowsExactly(NoSuchElementException.class,
				() -> company.removeEmployee(Constants.ID2));
		assertThrowsExactly(NoSuchElementException.class,
				() -> company.removeEmployee(Constants.ID3));
		assertEquals(0, company.getDepartmentBudget(Constants.DEPARTMENT2));
		assertArrayEquals(new Manager[0], company.getManagersWithMostFactor());
		assertArrayEquals(new String[] { Constants.DEPARTMENT1 }, company.getDepartments());
	}

	@Test
	void jsonTest() {
		Employee wageEmployee = createAndCompareEmployee(
				"{\"hours\":10,\"basicSalary\":1000,\"className\":\"telran.employees.WageEmployee\",\"id\":123,\"department\":\"QA\",\"wage\":100}",
				new WageEmployee(Constants.ID1, Constants.SALARY1, Constants.DEPARTMENT1, Constants.WAGE1,
						Constants.HOURS1));

		Employee manager = createAndCompareEmployee(
				"{\"basicSalary\":2000,\"className\":\"telran.employees.Manager\",\"id\":120,\"department\":\"QA\",\"factor\":2}",
				new Manager(Constants.ID2, Constants.SALARY2, Constants.DEPARTMENT1, Constants.FACTOR1));

		Employee salesPerson = createAndCompareEmployee(
				"{\"hours\":10,\"basicSalary\":3000,\"className\":\"telran.employees.SalesPerson\",\"id\":125,\"department\":\"Development\",\"percent\":0.01,\"sales\":10000,\"wage\":100}",
				new SalesPerson(Constants.ID3, Constants.SALARY3, Constants.DEPARTMENT2, Constants.WAGE1,
						Constants.HOURS1, Constants.PERCENT1, Constants.SALES1));
	}

	private Employee createAndCompareEmployee(String json, Employee expectedEmployee) {
		Employee employee = Employee.getEmployeeFromJSON(json);
		assertEquals(expectedEmployee, employee);
		return employee;
	}

	@Test
	void persistenceTest() {
		if (company instanceof Persistable persCompany) {
			CompanyImpl comp = new CompanyImpl();
			persCompany.saveToFile(Constants.DATA_FILE_NAME);
			comp.restoreFromFile(Constants.DATA_FILE_NAME);
			runTestIterator(comp);
		}
	}
}
