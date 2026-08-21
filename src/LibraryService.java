public interface LibraryService {

    void addBook(Book book) throws LibraryException;
    void registerMember(Member member) throws LibraryException;
    Book findBookByIsbn(String isbn) throws LibraryException;
    Member findMemberById(int memberId) throws LibraryException;
    boolean borrowBook(String isbn, int memberId, String borrowDateTime) throws LibraryException;
    int returnBook(String isbn, int memberId, String returnDateStr) throws LibraryException;

    Book[] getBooks();
    Member[] getMembers();
    int getBookCount();
    int getMemberCount();
}
