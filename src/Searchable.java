public interface Searchable {

    Book searchByIsbn(String isbn);
    void displaySearchResults(String isbn);
}
