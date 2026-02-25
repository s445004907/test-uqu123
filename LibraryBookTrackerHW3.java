import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class LibraryBookTrackerHW3 {
    private static List<Book> catalog = new ArrayList<>();
    private static int correctRecords = 0;
    private static int errorsEncountered = 0;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java LibraryBookTrackerHW3 <catalog_file> <command> [args]");
            return;
        }

        String fileName = args[0];
        String command = args[1];

        Thread readerThread = new Thread(() -> {
            System.out.println("Thread 1: Reading catalog...");
            try (Scanner scanner = new Scanner(new File(fileName))) {
                while (scanner.hasNextLine()) {
                    try {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",");
                        if (parts.length < 4) throw new Exception("Malformed entry: " + line);
                        
                        catalog.add(new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3])));
                        correctRecords++;
                    } catch (Exception e) {
                        errorsEncountered++;
                        logError("Catalog Error: " + e.getMessage());
                    }
                }
            } catch (FileNotFoundException e) {
                logError("File not found: " + fileName);
            }
        });

        Thread analyzerThread = new Thread(() -> {
            try {
                Thread.sleep(100); 
                System.out.println("Thread 2: Analyzing request...");
                
                if (command.equalsIgnoreCase("search") && args.length > 2) {
                    searchBook(args[2]);
                } else if (command.equalsIgnoreCase("add") && args.length > 2) {
                    System.out.println("Adding book functionality triggered: " + args[2]);
                }
            } catch (Exception e) {
                errorsEncountered++;
                logError("Analysis Error: " + e.getMessage());
            }
        });

        try {
            readerThread.start();
            readerThread.join(); 
            
            analyzerThread.start();
            analyzerThread.join(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            printStatistics();
            System.out.println("Thank you for using the Library Book Tracker");
        }
    }

    private static void searchBook(String title) {
        boolean found = false;
        for (Book b : catalog) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                System.out.println("Found: " + b);
                found = true;
                break;
            }
        }
        if (!found) System.out.println("Book not found: " + title);
    }

    private static void logError(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("errors.log", true))) {
            writer.println(LocalDateTime.now() + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStatistics() {
        System.out.println("\n--- Final Statistics ---");
        System.out.println("Correct Records: " + correctRecords);
        System.out.println("Errors Encountered: " + errorsEncountered);
    }
}