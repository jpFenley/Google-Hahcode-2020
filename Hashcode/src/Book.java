public class Book implements Comparable<Book> {
  public int ID;
  public int score;
  public boolean scanned = false;
  
  public Book(int ID, int score) {
    this.ID = ID;
    this.score = score;
  }
  
  public int compareTo(Book other) {
    return other.score - this.score;
  }
}
