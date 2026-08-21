import java.util.Scanner;

public class MenuHandler {

    // Depend on Interface abstraction for loose coupling
    private final LibraryService libraryService;

    public MenuHandler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    //---- Menu display method-------------------//
    public void displayMenu() {
        System.out.println("======================= LibraTrack ===============");
        System.out.println("1. Search About The Particular Book");
        System.out.println("2. Borrow Book To Particular Member");
        System.out.println("3. Return The Borrowed Book");
        System.out.println("4. View Amount of Fines");
        System.out.println("5. Librarian: Add Book");
        System.out.println("6. Librarian: Register Member");
        System.out.println("7. Library Info");
        System.out.println("0. Exit");
        System.out.println("======================");
    }

    public void processChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1: searchBook(scanner); break;
            case 2: borrowBook(scanner); break;
            case 3: returnBook(scanner); break;
            case 4: viewFines(); break;
            case 5: addBook(scanner); break;
            case 6: registerMember(scanner); break;
            case 7: displaySystemInfo(); break;
            case 0: System.out.println("Exiting LibraTrack. Goodbye!"); break;
            default: System.out.println("Invalid choice! Please select between 0 and 7.\n"); break;
        }
    }

    //---- Search method to search the book based on isbn------------------//
    private void searchBook(Scanner scanner) {
        System.out.println("\n--- Search Book ---");

        if (libraryService.getBookCount() == 0) {
            System.out.println("No books currently in system.\n");
            return;
        }

        System.out.print("Enter Book ISBN to search: ");
        String searchIsbn = scanner.next();

        if (libraryService instanceof Searchable) {
            ((Searchable) libraryService).displaySearchResults(searchIsbn);
        }
        System.out.println();
    }


    private void borrowBook(Scanner scanner) {
        System.out.println("\n--- Borrow Book ---");
        if (libraryService.getBookCount() == 0 || libraryService.getMemberCount() == 0) {
            System.out.println("Catalog or members list empty.\n");
            return;
        }

        try {
            System.out.print("Enter Book ISBN to borrow: ");
            String targetIsbn = scanner.next();

            System.out.print("Enter Member ID borrowing the book: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid ID format! Please enter an integer.");
                System.out.print("Enter Member ID: ");
                scanner.next();
            }
            int targetMemberId = scanner.nextInt();

            scanner.nextLine();
            System.out.print("Enter borrowing Date and Time (e.g. 2026-08-21 14:30): ");
            String dateTime = scanner.nextLine();

            libraryService.borrowBook(targetIsbn, targetMemberId, dateTime);

            Book book = libraryService.findBookByIsbn(targetIsbn);
            Member member = libraryService.findMemberById(targetMemberId);
            String dueDate = calculateDueDate(dateTime);

            System.out.println("\n==================================================");
            System.out.println("Success: Book '" + book.getTitle() + "' borrowed by " + member.getName() + ".");
            System.out.println("Borrow Date: " + dateTime);
            System.out.println("IMPORTANT: Return on or before " + dueDate + " to avoid fines!");
            System.out.println("==================================================\n");

        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void returnBook(Scanner scanner) {
        System.out.println("\n--- Return Book ---");
        if (libraryService.getBookCount() == 0) {
            System.out.println("No books currently registered in the system.\n");
            return;
        }

        try {
            System.out.print("Enter Book ISBN to return: ");
            String targetIsbn = scanner.next();

            System.out.print("Enter Member ID returning the book: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid ID format! Please enter an integer.");
                scanner.next();
            }
            int targetMemberId = scanner.nextInt();

            scanner.nextLine();
            System.out.print("Enter Return Date (YYYY-MM-DD): ");
            String returnDateStr = scanner.nextLine();

            Book book = libraryService.findBookByIsbn(targetIsbn);
            Member member = libraryService.findMemberById(targetMemberId);


            int totalDays = libraryService.returnBook(targetIsbn, targetMemberId, returnDateStr);

            System.out.println("\n==================================================");
            System.out.println("Success: Book '" + book.getTitle() + "' returned by " + member.getName() + ".");
            System.out.println("Total Days Kept: " + totalDays + " days");
            if (totalDays > 30) {
                int overdue = totalDays - 30;
                System.out.println("OVERDUE: Returned " + overdue + " days late! Fine: ₹" + (overdue * 100));
            } else {
                System.out.println("Returned on time!");
            }
            System.out.println("==================================================\n");

        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void viewFines() {
        System.out.println("\n================================ MEMBER FINES REPORT ================================");
        int memberCount = libraryService.getMemberCount();
        if (memberCount == 0) {
            System.out.println("No members registered in the system.\n");
            return;
        }

        System.out.printf("%-12s | %-25s | %-15s | %-15s%n", "Member ID", "Name", "Books Borrowed", "Fine Amount");
        System.out.println("-------------------------------------------------------------------------------------");

        Member[] members = libraryService.getMembers();
        boolean hasFines = false;
        for (int i = 0; i < memberCount; i++) {
            Member m = members[i];
            System.out.printf("%-12d | %-25s | %-15d | ₹%-14.2f%n",
                    m.getMemberId(),
                    m.getName(),
                    m.getBooksBorrowedCount(),
                    m.getFineAmount());

            if (m.getFineAmount() > 0) {
                hasFines = true;
            }
        }

        System.out.println("-------------------------------------------------------------------------------------");
        if (!hasFines) {
            System.out.println("All members have clean records! Total fines across all members: ₹0.00");
        }
        System.out.println("=====================================================================================\n");
    }

    private void addBook(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter ISBN: "); String isbn = scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Author Name: "); String author = scanner.nextLine();
        System.out.print("Enter Price: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid price! Please enter a valid decimal number.");
            System.out.print("Enter Price: ");
            scanner.next();
        }
        double price = scanner.nextDouble();

        try {
            libraryService.addBook(new Book(isbn, title, author, price));
            System.out.println("Book added successfully!\n");
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void registerMember(Scanner scanner) {
        System.out.print("Enter Member ID (Integer): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid ID! Please enter an integer.");
            System.out.print("Enter Member ID (Integer): ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Member Name: ");
        String name = scanner.nextLine();

        try {
            libraryService.registerMember(new Member(id, name));
            System.out.println("Member registered successfully!\n");
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void displaySystemInfo() {
        System.out.println("\n================================ SYSTEM INFORMATION ================================");
        System.out.println("Total books: " + Book.getBookCount());
        System.out.println("Total no of members: " + Member.getMemberCount());
        System.out.println("Active Books in Array: " + libraryService.getBookCount());
        System.out.println("Active Members in Array: " + libraryService.getMemberCount());
        System.out.println("====================================================================================\n");

        System.out.println("----------------------------------- BOOKS LIST -----------------------------------");
        int bookCount = libraryService.getBookCount();
        if (bookCount == 0) {
            System.out.println("No books currently registered in the system.");
        } else {
            System.out.printf("%-12s | %-25s | %-20s | %-10s | %-10s%n", "ISBN", "Title", "Author", "Price", "Available");
            System.out.println("----------------------------------------------------------------------------------");
            Book[] books = libraryService.getBooks();
            for (int i = 0; i < bookCount; i++) {
                Book b = books[i];
                System.out.printf("%-12s | %-25s | %-20s | $%-9.2f | %-10s%n",
                        b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPrice(), b.isAvailable() ? "Yes" : "No");
            }
        }
        System.out.println("----------------------------------------------------------------------------------\n");

        System.out.println("---------------------------------- MEMBERS LIST ----------------------------------");
        int memberCount = libraryService.getMemberCount();
        if (memberCount == 0) {
            System.out.println("No members currently registered in the system.");
        } else {
            System.out.printf("%-12s | %-25s | %-22s%n", "Member ID", "Name", "Books Borrowed");
            System.out.println("----------------------------------------------------------------------------------");
            Member[] members = libraryService.getMembers();
            for (int i = 0; i < memberCount; i++) {
                Member m = members[i];
                System.out.printf("%-12d | %-25s | %-22d%n",
                        m.getMemberId(), m.getName(), m.getBooksBorrowedCount());
            }
        }
        System.out.println("----------------------------------------------------------------------------------\n");
    }

    private String calculateDueDate(String borrowDateTime) {
        try {
            String trimmed = borrowDateTime.trim();
            String datePart = trimmed.contains(" ") ? trimmed.split("\\s+")[0] : trimmed;
            String timePart = trimmed.contains(" ") ? " " + trimmed.split("\\s+")[1] : "";

            String[] parts = datePart.split("-");
            if (parts.length != 3) return borrowDateTime + " (Due in 30 days)";

            int year = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim());
            int day = Integer.parseInt(parts[2].trim()) + 30;

            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) daysInMonth[2] = 29;

            while (day > daysInMonth[month]) {
                day -= daysInMonth[month];
                month++;
                if (month > 12) { month = 1; year++; }
            }
            return String.format("%04d-%02d-%02d", year, month, day) + timePart;
        } catch (Exception e) {
            return borrowDateTime + " (Due in 30 days)";
        }
    }
}