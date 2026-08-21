public class Library implements LibraryService, Searchable {

    private Book[] books = new Book[100];
    private Member[] members = new Member[100];
    private int currentBookIndex = 0;
    private int currentMemberIndex = 0;

    @Override
    public void addBook(Book book) throws LibraryException {
        if (currentBookIndex >= books.length) {
            throw new LibraryException("Storage full! Cannot add more books.");
        }
        books[currentBookIndex++] = book;
    }

    @Override
    public void registerMember(Member member) throws LibraryException {
        if (currentMemberIndex >= members.length) {
            throw new LibraryException("Storage full! Cannot register more members.");
        }
        members[currentMemberIndex++] = member;
    }

    @Override
    public Book findBookByIsbn(String isbn) throws LibraryException {
        for (int i = 0; i < currentBookIndex; i++) {
            if (books[i].getIsbn().equalsIgnoreCase(isbn)) {
                return books[i];
            }
        }
        throw new LibraryException("No book found with ISBN: " + isbn);
    }

    @Override
    public Member findMemberById(int memberId) throws LibraryException {
        for (int i = 0; i < currentMemberIndex; i++) {
            if (members[i].getMemberId() == memberId) {
                return members[i];
            }
        }
        throw new LibraryException("No member registered with ID: " + memberId);
    }

    @Override
    public Book searchByIsbn(String isbn) {
        try {
            return findBookByIsbn(isbn);
        } catch (LibraryException e) {
            return null;
        }
    }

    @Override
    public void displaySearchResults(String isbn) {
        try {
            Book b = findBookByIsbn(isbn);
            System.out.println("\nBook found!");
            System.out.printf("%-12s | %-25s | %-20s | %-10s | %-10s%n", "ISBN", "Title", "Author", "Price", "Available");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.printf("%-12s | %-25s | %-20s | $%-9.2f | %-10s%n",
                    b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPrice(), b.isAvailable() ? "Yes" : "No");
        } catch (LibraryException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean borrowBook(String isbn, int memberId, String borrowDateTime) throws LibraryException {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (!book.isAvailable()) {
            throw new LibraryException("Book '" + book.getTitle() + "' is currently already borrowed.");
        }

        book.setAvailable(false);
        book.setBorrowedDateTime(borrowDateTime);
        member.setBooksBorrowedCount(member.getBooksBorrowedCount() + 1);
        return true;
    }

    @Override
    public int returnBook(String isbn, int memberId, String returnDateStr) throws LibraryException {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book.isAvailable()) {
            throw new LibraryException("Book '" + book.getTitle() + "' is not currently borrowed.");
        }

        String borrowDateStr = book.getBorrowedDateTime();
        int startDays = dateToAbsoluteDays(borrowDateStr);
        int endDays = dateToAbsoluteDays(returnDateStr);
        int totalDays = endDays - startDays;

        if (totalDays > 30) {
            int overdueDays = totalDays - 30;
            int fineAmount = overdueDays * 100;
            member.setFineAmount(member.getFineAmount() + fineAmount);
        }

        book.setAvailable(true);
        book.setBorrowedDateTime(null);
        if (member.getBooksBorrowedCount() > 0) {
            member.setBooksBorrowedCount(member.getBooksBorrowedCount() - 1);
        }

        return totalDays;
    }

    @Override
    public Book[] getBooks() { return books; }

    @Override
    public Member[] getMembers() { return members; }

    @Override
    public int getBookCount() { return currentBookIndex; }

    @Override
    public int getMemberCount() { return currentMemberIndex; }

    private int dateToAbsoluteDays(String dateStr) {
        String trimmed = dateStr.trim();
        if (trimmed.contains(" ")) {
            trimmed = trimmed.split("\\s+")[0];
        }
        String[] parts = trimmed.split("-");
        if (parts.length != 3) return 0;

        int year = Integer.parseInt(parts[0].trim());
        int month = Integer.parseInt(parts[1].trim());
        int day = Integer.parseInt(parts[2].trim());

        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int totalDays = year * 365 + (year / 4) - (year / 100) + (year / 400);

        for (int i = 1; i < month; i++) {
            if (i == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
                totalDays += 29;
            } else {
                totalDays += daysInMonth[i];
            }
        }
        totalDays += day;
        return totalDays;
    }
}