package telran.employees;

import telran.io.Persistable;

public class CompanySaveData extends Thread {
    private Persistable company;
    private String fileName;
    private long timeInterval;

    public CompanySaveData(Persistable company, String fileName, long timeInterval) {
        this.company = company;
        this.fileName = fileName;
        this.timeInterval = timeInterval;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                company.saveToFile(fileName);
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
