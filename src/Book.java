public class Book {

    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;
    private double price;
    private String borrowedDateTime;

    //---- Counter that will count the number of books
    private static int bookCount = 0;

    // ------ Default Constructor
    public Book() {
        this("UNKNOWN", "Untitled", "Unknown Author", 0.0);
    }

    //----------  Overloaded Constructor (Parametrized constructor that will use to initialize the object)
    public Book(String isbn, String title, String author, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isAvailable = true;
        bookCount++;
    }

    // Varargs method to process/display incoming books
    public static void displayBookBatch(Book... books) {
        System.out.println("--- Batch Book Processing ---");
        for (int i = 0; i < books.length; i++) {
            if (books[i] != null) {
                books[i].displayInfo();
            }
        }
    }

    //---- Display method that will display the information of the books
    public void displayInfo() {
        System.out.println("ISBN: " + isbn + " | Title: " + title + " | Author: " + author +
                " | Price: $" + price + " | Available: " + isAvailable);
    }

    //------- Getters and Setters(to get & set the values of the books)
    public String getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public double getPrice() {
        return price;
    }
    public static int getBookCount() {
        return bookCount;
    }
    public String getBorrowedDateTime() {
        return borrowedDateTime;
    }
    public void setBorrowedDateTime(String borrowedDateTime) {
        this.borrowedDateTime = borrowedDateTime;
    }
}
