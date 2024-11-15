package telran.employees;

import java.nio.channels.NetworkChannel;
import java.util.Iterator;
import java.util.stream.IntStream;

import org.json.JSONArray;

import telran.net.NetworkClient;
import telran.net.TcpClient;

public class CompanyNetProxy implements Company{
    NetworkClient netClient;

    public CompanyNetProxy(NetworkClient netClient) {
        this.netClient = netClient;
    }

    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        netClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String budgetString = netClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(budgetString);
    }

    @Override
    public String[] getDepartments() {
        String jsonString = netClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonString);
        return jsonArray.toList().toArray(String[]::new);
    }

    @Override
    public Employee getEmployee(long id) {
        String jsonString = netClient.sendAndReceive("getEmployee", String.valueOf(id));   
        return Employee.getEmployeeFromJSON(jsonString);

    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String jsonString = netClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray jsonArray = new JSONArray(jsonString);

        return jsonArray.toList().stream()
            .map(obj -> Employee.getEmployeeFromJSON(obj.toString()))
            .toArray(Manager[]::new);
    }

    @Override
    public Employee removeEmployee(long id) {
        String jsonString = netClient.sendAndReceive("removeEmployee", String.valueOf(id));   
        return Employee.getEmployeeFromJSON(jsonString);    
    }

}
