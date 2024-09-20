package telran.employees;

public class SalesPerson extends WageEmployee {
    float percent;
    long sales;

    public SalesPerson(long id, int basicSalary, String department, int wage, int hours, float percent, long sales) {
        super(id, basicSalary, department, wage, hours);
        this.percent = percent;
        this.sales = sales;
    }

    @Override
    public int computeSalary() {
        return (int) (super.computeSalary() + sales * percent / 100);
    }
}
