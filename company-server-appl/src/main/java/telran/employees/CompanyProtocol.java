package telran.employees;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol {

    Company company;

    public CompanyProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response = null;
        try {
            Method method = CompanyProtocol.class.getDeclaredMethod(type, String.class);
            method.setAccessible(true);
            return (Response) method.invoke(this, data);
        } catch (NoSuchMethodException e) {
            response = new Response(ResponseCode.WRONG_DATA, type + " Not found");
        } catch (Exception e) {
            Throwable causeExc = e.getCause();
            String message = causeExc == null ? e.getMessage() : causeExc.getMessage();
            response = new Response(ResponseCode.WRONG_DATA, message);
        }
        return response;
    }

    private Response removeEmployee(String data) {
        Employee employee = company.removeEmployee(Long.parseLong(data));
        return new Response(ResponseCode.OK, employee.toString());
    }

    private Response getManagersWithMostFactor(String data) {
        Manager[] managers = company.getManagersWithMostFactor();
        String[] strings = Arrays.stream(managers).map(Manager::toString).toArray(String[]::new);
        JSONArray jsonArray = new JSONArray(strings);

        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response getEmployee(String data) {
        Employee empl = company.getEmployee(Long.parseLong(data));
        return new Response(ResponseCode.OK, empl.toString());
    }

    private Response getDepartments(String data) {
        String[] departments = company.getDepartments();
        JSONArray jsonArray = new JSONArray(departments);
        return new Response(ResponseCode.OK, jsonArray.toString());
    }

    private Response getDepartmentBudget(String data) {
        int budget = company.getDepartmentBudget(data);
        return new Response(ResponseCode.OK, String.valueOf(budget));
    }

    private Response addEmployee(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        company.addEmployee(employee);
        return new Response(ResponseCode.OK, "");
    }

}
