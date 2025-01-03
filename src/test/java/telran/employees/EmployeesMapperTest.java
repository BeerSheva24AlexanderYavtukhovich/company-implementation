package telran.employees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import telran.employees.db.jpa.EmployeeEntity;
import telran.employees.db.jpa.EmployeesMapper;

public class EmployeesMapperTest {
    private void assertEmployeeConversion(Employee expected) {
        EmployeeEntity employeeEntity = EmployeesMapper.toEmployeeEntityFromDto(expected);
        Employee actual = EmployeesMapper.toEmployeeDtoFromEntity(employeeEntity);
        assertEquals(expected.getClass().getSimpleName(), actual.getClass().getSimpleName());
    }

    @Test
    public void MapperTest() {
        assertEmployeeConversion(new WageEmployee(Constants.ID1, Constants.SALARY1, Constants.DEPARTMENT1,
                Constants.WAGE1, Constants.HOURS1));
        assertEmployeeConversion(
                new Manager(Constants.ID2, Constants.SALARY2, Constants.DEPARTMENT1, Constants.FACTOR1));
        assertEmployeeConversion(new SalesPerson(Constants.ID3, Constants.SALARY3, Constants.DEPARTMENT2,
                Constants.WAGE1, Constants.HOURS1, Constants.PERCENT1, Constants.SALES1));
    }
}