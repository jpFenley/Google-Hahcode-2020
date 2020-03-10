import java.util.PriorityQueue;

public class Library {
      int ID;
      int numBooks;
      int daysSetup;
      int booksPerDay;
      boolean active = false;
      
      Book[] books;
      
      int daysRemainingSignup;
      
      public Library(int numBooks, int daysSetup, int booksPerDay, int ID) {
        this.numBooks = numBooks;
        this.daysSetup = daysSetup;
        this.booksPerDay = booksPerDay;
        this.ID = ID;
        daysRemainingSignup = daysSetup;
      }
      
 }