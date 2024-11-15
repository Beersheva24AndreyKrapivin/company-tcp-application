package telran.employees;

import telran.io.Persistable;

public class CompanySaveData implements Runnable {
    private Persistable company;
    private String fileName;

    public CompanySaveData(Persistable company, String fileName) {
        this.company = company;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                company.saveToFile(fileName);
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
