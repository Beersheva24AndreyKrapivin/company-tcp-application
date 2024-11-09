package telran.employees;

import telran.net.TcpServer;

public class Main {
    private static final int PORT = 4000;

    public static void main(String[] args) {
        CompanyProtocol companyProtocol = new CompanyProtocol();
        TcpServer tcpServer = new TcpServer(companyProtocol, PORT);
        tcpServer.run();
    }
}