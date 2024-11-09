package telran.employees;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol {

    private static Company company = new CompanyImpl();

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response = null;
        try {
            response = switch (type) {
                case "addEmployee" -> addEmployeeServer(data);
                case "getDepartmentBudget" -> getDepartmentBudgetServer(data);
                case "getDepartments" -> getDepartmentsServer();
                case "getEmployee" -> getEmployeeServer(data);
                case "getManagersWithMostFactor" -> getManagersWithMostFactorServer();
                case "removeEmployee" -> removeEmployeeServer(data);
                default -> new Response(ResponseCode.WRONG_TYPE, type + " is wrong type");
            };
        } catch (Exception e) {
            response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }
        return response;
    }

    private Response removeEmployeeServer(String data) {
        Employee employee = company.removeEmployee(Long.parseLong(data));
        return new Response(ResponseCode.OK, employee.toString());
    }

    private Response getManagersWithMostFactorServer() {
        Manager[] managers = company.getManagersWithMostFactor();
        String[] strings = Arrays.stream(managers).map(Manager::toString).toArray(String[]::new);
        JSONArray jsonArray = new JSONArray(strings);

        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response getEmployeeServer(String data) {
        Employee empl = company.getEmployee(Long.parseLong(data));
        return new Response(ResponseCode.OK, empl.toString());
    }

    private Response getDepartmentsServer() {
        String[] departments = company.getDepartments();
        JSONArray jsonArray = new JSONArray(departments);
        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response getDepartmentBudgetServer(String data) {
        int budget = company.getDepartmentBudget(data);
        return new Response(ResponseCode.OK, String.valueOf(budget));
    }

    private Response addEmployeeServer(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        company.addEmployee(employee);
        return new Response(ResponseCode.OK, "");
    }

}
