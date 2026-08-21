public class Member {

    private int memberId;
    private String name;
    private int booksBorrowedCount;
    private double fineAmount;

    //----- Static counter that will count the total no of members registered
    private static int memberCount = 0;

    //----- Paramterized constructor(used to initalize the values of members)
    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.booksBorrowedCount = 0;
        this.fineAmount = 0.0;
        memberCount++;
    }

    //--- Display method that is used to display the details of the members
    public void displayInfo() {
        System.out.println("Member ID: " + memberId + " | Name: " + name +
                " | Books Borrowed: " + booksBorrowedCount);
    }

    //----  Getters and Setters
    public int getMemberId() {
        return memberId;
    }
    public String getName() {
        return name;
    }
    public int getBooksBorrowedCount() {
        return booksBorrowedCount;
    }
    public void setBooksBorrowedCount(int count) {
        this.booksBorrowedCount = count;
    }
    public static int getMemberCount() {
        return memberCount;
    }
    public double getFineAmount() {
        return fineAmount;
    }
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
}
