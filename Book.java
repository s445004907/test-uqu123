class Book {
    private String title;
    private String author;
    private String isbn;
    private int copies;

    public Book(String title, String author, String isbn, int copies) throws MalformedBookEntryException {
        
        if (title.isEmpty() || author.isEmpty()) {
            throw new MalformedBookEntryException("Title and Author cannot be empty.");
        }
        
        if (!isbn.matches("\\d{13}")) {
            throw new MalformedBookEntryException("ISBN must be exactly 13 digits.");
        }
        
        if (copies <= 0) {
            throw new MalformedBookEntryException("Copies must be a positive integer.");
        }
        
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
    }

    
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getCopies() { return copies; }

    @Override
    public String toString() {
        return String.format("%s:%s:%s:%d", title, author, isbn, copies);
    }
}
