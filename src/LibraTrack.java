import java.util.Scanner;

public class LibraTrack {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //-----------  Pass concrete implementation through interface type
        LibraryService libraryService = new Library();
        MenuHandler menuHandler = new MenuHandler(libraryService);

        int choice = -1;

        do {
            menuHandler.displayMenu();
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                menuHandler.processChoice(choice, scanner);
            } else {
                System.out.println("Invalid input! Please enter a valid numerical option.\n");
                scanner.next();
            }

        } while (choice != 0);
        scanner.close();
    }
}