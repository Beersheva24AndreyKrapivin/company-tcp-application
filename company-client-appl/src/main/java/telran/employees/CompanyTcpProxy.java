package telran.employees;

import java.util.Iterator;
import java.util.stream.IntStream;

import org.json.JSONArray;

import telran.net.TcpClient;

public class CompanyTcpProxy implements Company{
    TcpClient tcpClient;

    public CompanyTcpProxy(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public Iterator<Employee> iterator() {
        CompanyImpl impl = new CompanyImpl();
        return impl.iterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        tcpClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String budgetString = tcpClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(budgetString);
    }

    @Override
    public String[] getDepartments() {
        String jsonString = tcpClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonString);
        return jsonArray.toList().toArray(String[]::new);
    }

    @Override
    public Employee getEmployee(long id) {
        String jsonString = tcpClient.sendAndReceive("getEmployee", String.valueOf(id));   
        return Employee.getEmployeeFromJSON(jsonString);

    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String jsonString = tcpClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray jsonArray = new JSONArray(jsonString);

        return jsonArray.toList().stream()
            .map(obj -> Employee.getEmployeeFromJSON(obj.toString()))
            .toArray(Manager[]::new);
    }

    @Override
    public Employee removeEmployee(long id) {
        String jsonString = tcpClient.sendAndReceive("removeEmployee", String.valueOf(id));   
        return Employee.getEmployeeFromJSON(jsonString);    
    }

}
