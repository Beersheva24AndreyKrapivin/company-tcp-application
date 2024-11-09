package telran.employees;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import telran.view.*;

public class CompanyItems {

    final static long MIN_ID = 100000;
    final static long MAX_ID = 999999;
    final static int MIN_SALARY = 5000;
    final static int MAX_SALARY = 30000;
    final static String[] DEPARTMENTS = { "QA", "Audit", "Development", "Management" };
    final static int MIN_WAGE = 20;
    final static int MAX_WAGE = 40;
    final static int MIN_HOURS = 1;
    final static int MAX_HOURS = 20;
    final static float MIN_PERCENT = 1;
    final static float MAX_PERCENT = 10;
    final static long MIN_SALES = 10_000;
    final static long MAX_SALES = 1_000_000;
    final static float MIN_FACTOR = 0.5f;
    final static float MAX_FACTOR = 5f;
    final static String FILE_NAME = "config";

    private static Company company;

    public static Item[] getItems(Company company) {
        CompanyItems.company = company;
        Item[] res = {
                Item.of("Add Employee", CompanyItems::showSubmenuEmployee),
                Item.of("Display Employee Data", CompanyItems::displayEmployeeData),
                Item.of("Fire Employee", CompanyItems::fireEmployee),
                Item.of("Department Salary Budget", CompanyItems::departmentSalaryBudget),
                Item.of("List of Departments", CompanyItems::listOfDepartments),
                Item.of("Display Managers with Most Factor", CompanyItems::displayManagersFactor)
        };
        return res;
    }

    static void showSubmenuEmployee(InputOutput io) {
        Item[] items = getItemsForMenuEmployee();
        Menu menu = new Menu("Add Employee", items);
        menu.perform(io);
    }

    private static Item[] getItemsForMenuEmployee() {
        Item[] res = {
                Item.of("Hire Employee", CompanyItems::hireEmployee),
                Item.of("Hire Wage Employee", CompanyItems::hireWageEmployee),
                Item.of("Hire Sales Person", CompanyItems::hireSalesPerson),
                Item.of("Hire Manager", CompanyItems::hireManager),
                Item.ofExit()
        };
        return res;
    }

    private static String enterDepartment(InputOutput io) {
        HashSet<String> departmentsSet = new HashSet<>(List.of(DEPARTMENTS));
        return io.readStringOptions("Enter department from " + departmentsSet, "Must be one out from " + departmentsSet,
                departmentsSet);
    }

    private static int enterSalary(InputOutput io) {
        return io.readNumberRange(String.format("Enter Salary value in the range [%d-%d]", MIN_SALARY, MAX_SALARY),
                "Wrong Salary value", MIN_SALARY, MAX_SALARY).intValue();
    }

    private static int enterWage(InputOutput io) {
        return io.readNumberRange(String.format("Enter Wage value in the range [%d-%d]", MIN_WAGE, MAX_WAGE),
                "Wrong Wage value", MIN_WAGE, MAX_WAGE).intValue();
    }

    private static int enterHours(InputOutput io) {
        return io.readNumberRange(String.format("Enter Hours value in the range [%d-%d]", MIN_HOURS, MAX_HOURS),
                "Wrong Hours value", MIN_HOURS, MAX_HOURS).intValue();
    }

    private static long enterId(InputOutput io) {
        return io.readNumberRange(String.format("Enter ID value in the range [%d-%d]", MIN_ID, MAX_ID),
                "Wrong ID value", MIN_ID, MAX_ID).longValue();
    }

    private static float enterPercent(InputOutput io) {
        return io.readNumberRange(String.format("Enter Percent value in the range [%f-%f]", MIN_PERCENT, MAX_PERCENT),
                "Wrong Percent value", MIN_PERCENT, MAX_PERCENT).floatValue();
    }

    private static long enterSales(InputOutput io) {
        return io.readNumberRange(String.format("Enter Sales value in the range [%d-%d]", MIN_SALES, MAX_SALES),
                "Wrong Sales value", MIN_SALES, MAX_SALES).longValue();
    }

    private static float enterFactor(InputOutput io) {
        return io.readNumberRange(String.format("Enter Factor value in the range [%f-%f]", MIN_FACTOR, MAX_FACTOR),
                "Wrong Factor value", MIN_FACTOR, MAX_FACTOR).floatValue();
    }

    private static void addEmployeeInCompany(InputOutput io, Employee empl, String typeEmployee) {
        company.addEmployee(empl);
        io.writeLine(typeEmployee + " " + empl + " was added");
    }

    private static Employee createEmployee(InputOutput io) {
        long id = enterId(io);
        int salary = enterSalary(io);
        String department = enterDepartment(io);
        return new Employee(id, salary, department);
    }

    private static Employee createWageEmployee(InputOutput io) {
        Employee empl = createEmployee(io);
        int wage = enterWage(io);
        int hours = enterHours(io);
        return new WageEmployee(empl.getId(), empl.computeSalary(), empl.getDepartment(), wage, hours);
    }

    private static Employee createSalesPerson(InputOutput io) {
        Employee empl = createEmployee(io);
        int wage = enterWage(io);
        int hours = enterHours(io);
        float percent = enterPercent(io);
        long sales = enterSales(io);
        return new SalesPerson(empl.getId(), empl.computeSalary(), empl.getDepartment(), wage, hours, percent, sales);
    }

    private static Employee createManager(InputOutput io) {
        Employee empl = createEmployee(io);
        float factor = enterFactor(io);
        return new Manager(empl.getId(), empl.computeSalary(), empl.getDepartment(), factor);
    }

    static void hireEmployee(InputOutput io) {
        Employee empl = createEmployee(io);
        addEmployeeInCompany(io, empl, "Employee");
    }

    static void hireWageEmployee(InputOutput io) {
        Employee empl = createWageEmployee(io);
        addEmployeeInCompany(io, empl, "Wage employee");
    }

    static void hireSalesPerson(InputOutput io) {
        Employee empl = createSalesPerson(io);
        addEmployeeInCompany(io, empl, "Sales employee");
    }

    static void hireManager(InputOutput io) {
        Employee empl = createManager(io);
        addEmployeeInCompany(io, empl, "Manager");
    }

    static void displayEmployeeData(InputOutput io) {
        long id = enterId(io);
        Employee empl = company.getEmployee(id);
        if (empl == null) {
            io.writeLine("Employee with ID " + id + " was not found");
        } else {
            io.writeLine(empl);
        }
    }

    static void fireEmployee(InputOutput io) {
        long id = enterId(io);
        Employee empl = company.removeEmployee(id);
        io.writeLine("Employee " + empl + " was fired");
    }

    static void departmentSalaryBudget(InputOutput io) {
        String department = enterDepartment(io);
        io.writeLine(department + " budget = " + company.getDepartmentBudget(department));
    }

    static void listOfDepartments(InputOutput io) {
        io.writeLine("List of departments: " + Arrays.toString(company.getDepartments()));
    }

    static void displayManagersFactor(InputOutput io) {
        io.writeLine("Managers having the most factor: " + Arrays.toString(company.getManagersWithMostFactor()));
    }

}
