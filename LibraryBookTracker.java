import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class LibraryBookTracker {

    public static void main(String[] args) {
        List<Book> inventory = new ArrayList<>();
        int validRecords = 0;
        int errorCount = 0;
        int searchResults = 0;
        int booksAdded = 0;
        File logFile = null;

        try {
           
            if (args.length < 2) {
                throw new InsufficientArgumentsException("Fewer than two command-line arguments.");
            }

            
            String fileName = args[0];
            if (!fileName.endsWith(".txt")) {
                throw new InvalidFileNameException("First argument does not end with .txt.");
            }

            File catalogFile = new File(fileName);
            File parentDir = catalogFile.getParentFile();
            
            
            if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
            if (!catalogFile.exists()) catalogFile.createNewFile();

            
            logFile = new File(parentDir != null ? parentDir : new File("."), "errors.log");

            
            try (Scanner fileScanner = new Scanner(catalogFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (line.trim().isEmpty()) continue;
                    try {
                        String[] parts = line.split(":");
                        if (parts.length != 4) throw new MalformedBookEntryException("Invalid fields count.");
                        
                       
                        Book book = new Book(parts[0].trim(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim()));
                        inventory.add(book);
                        validRecords++;
                    } catch (Exception e) {
                        
                        logError(logFile, line, e.toString());
                        errorCount++;
                    }
                }
            }

            
            String operation = args[1];
            if (operation.contains(":") && operation.split(":").length == 4) {
                
                booksAdded = addNewBook(operation, inventory, catalogFile, logFile);
            } else if (operation.matches("\\d{13}")) {
                
                searchResults = searchByIsbn(operation, inventory);
            } else {
                
                searchResults = searchByTitle(operation, inventory);
            }

        } catch (BookCatalogException e) {
            System.err.println("Error: " + e.getMessage());
            if (logFile != null && args.length >= 2) {
                logError(logFile, args[1], e.toString()); 
            }
            errorCount++;
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } finally {
            
            System.out.println("\n--- Statistics ---");
            System.out.println("Valid records processed: " + validRecords);
            System.out.println("Search results: " + searchResults);
            System.out.println("Books added: " + booksAdded);
            System.out.println("Errors encountered: " + errorCount);
            
            System.out.println("Thank you for using the Library Book Tracker.");
        }
    }

    
    public static int addNewBook(String data, List<Book> inventory, File file, File logFile) {
        try {
            String[] parts = data.split(":");
            Book newBook = new Book(parts[0].trim(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim()));
            inventory.add(newBook);
            
            
            inventory.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
            
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (Book b : inventory) {
                    writer.println(b.getTitle() + ":" + b.getAuthor() + ":" + b.getIsbn() + ":" + b.getCopies());
                }
            }
            System.out.println("Book added successfully:");
            printHeader();
            printBookRow(newBook);
            return 1;
        } catch (Exception e) {
            logError(logFile, data, e.toString());
            System.err.println("Failed to add book: " + e.getMessage());
            return 0;
        }
    }

    
    public static int searchByTitle(String keyword, List<Book> inventory) {
        printHeader();
        int count = 0;
        for (Book b : inventory) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                printBookRow(b);
                count++;
            }
        }
        return count;
    }

   
    public static int searchByIsbn(String isbn, List<Book> inventory) throws DuplicateISBNException {
        printHeader();
        List<Book> matches = new ArrayList<>();
        for (Book b : inventory) {
            if (b.getIsbn().equals(isbn)) matches.add(b);
        }
        
        if (matches.size() > 1) throw new DuplicateISBNException("Multiple books with same ISBN found.");
        if (!matches.isEmpty()) printBookRow(matches.get(0));
        return matches.size();
    }

    
    public static void printHeader() {
        System.out.printf("%-30s %-20s %-15s %5s%n", "Title", "Author", "ISBN", "Copies");
        System.out.println("-------------------------------------------------------------");
    }

    public static void printBookRow(Book b) {
        System.out.printf("%-30s %-20s %-15s %5d%n", b.getTitle(), b.getAuthor(), b.getIsbn(), b.getCopies());
    }

    
    public static void logError(File logFile, String text, String err) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.printf("[%s] INVALID: \"%s\" | %s%n", LocalDateTime.now(), text, err);
        } catch (IOException e) {
            System.err.println("Logging failed.");
        }
    }
}