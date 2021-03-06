package net.esve.bsc;

import net.esve.bsc.services.FileReadWrite;
import net.esve.bsc.services.OutputService;
import net.esve.bsc.services.PaymentsService;
import net.esve.bsc.services.UserInputListener;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;


/**
 * Created by Viliam on 05-May-16.
 */
public class PaymentTrackerApp {

    /**
     * @param args application args
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) {

        List<String> fileLines = null;
        FileReadWrite fileService = new FileReadWrite();

        List<String> arrArgs = Arrays.asList(args);
        if (arrArgs.size() == 1) {
            String path = args[0];
            fileLines = fileService.readFileLines(path);
        } else {
            System.out.println("Please type the path of payments filename and press ENTER");
            Scanner scanner = new Scanner(System.in);

            do {
                String s = scanner.nextLine();
                if (s.equals("")) {
                    System.out.println("We are continuing without payments file ...");
                    break;
                } else {
                    fileLines = fileService.readFileLines(s);
                    break;
                }
            } while (true);

        }

        if (fileLines != null && !fileLines.isEmpty()) {
            for (String line : fileLines) {
                PaymentsService.createRecord(line);
            }
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new OutputService(), 1, 10, SECONDS);

        UserInputListener inputService = new UserInputListener();
        Thread inputServiceBackgroundThread = new Thread(inputService);
        inputServiceBackgroundThread.start();

    }
}
