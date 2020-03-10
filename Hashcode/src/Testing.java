import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Testing {
  public static Book[] books; // All books
  public static Library[] libraries; // All libraries
  public static HashMap<Integer, ArrayList<Book>> assigned =
      new HashMap<Integer, ArrayList<Book>>();

  public static void main(String[] args) throws FileNotFoundException {
    File data = new File("c_incunabula.txt");
    Scanner scnr = new Scanner(data);

    int numBooks = scnr.nextInt();
    int numLibraries = scnr.nextInt();
    int days = scnr.nextInt();
    scnr.nextLine();

    books = new Book[numBooks];
    for (int i = 0; i < books.length; ++i) {
      Book newBook = new Book(i, scnr.nextInt());
      books[i] = newBook;
    }
    scnr.nextLine();

    libraries = new Library[numLibraries];
    for (int i = 0; i < libraries.length; ++i) {
      Library newLibrary = new Library(scnr.nextInt(), scnr.nextInt(), scnr.nextInt(), i);
      scnr.nextLine();
      newLibrary.books = new Book[newLibrary.numBooks];
      for (int j = 0; j < newLibrary.numBooks; ++j) {
        newLibrary.books[j] = books[scnr.nextInt()];
      }
      scnr.nextLine();
      Arrays.sort(newLibrary.books);
      libraries[i] = newLibrary;
    }


    Library signingUp = null;
    boolean signUpInProgress = false;
    ArrayList<Library> orderOpened = new ArrayList<Library>();

    for (int day = 0; day < days; ++day) {
      System.out.println("The day is: " + day + ", " + orderOpened.size() + " libraries are open.");
      if (signingUp != null) { // Check a library is signing up
        if (signingUp.daysRemainingSignup == 0) { // If done, then add it to list of open libraries
          signUpInProgress = false;
          orderOpened.add(signingUp);
          signingUp.active = true;
          signingUp = null;
        }
      }

      if (signUpInProgress == false) {
        signingUp = chooseLibrary(days - day);
      }

      assignBooks();

      if (signingUp != null) {
        signingUp.daysRemainingSignup -= 1;
      }
    }

    File output = new File("output3.txt");
    PrintWriter results = new PrintWriter(output);

    results.print(orderOpened.size() + "\n");
    for (Library lib : orderOpened) {
      ArrayList<Book> bookValues = assigned.get(lib.ID);
      System.out.println(bookValues + " b" + lib.ID);
      results.print(lib.ID + " " + bookValues.size() + "\n");
      for (Book b : bookValues) {
        results.print(b.ID + " ");
      }
      results.println();
    }
    results.close();
  }


  public static Library chooseLibrary(int daysLeft) {
    double max = Integer.MIN_VALUE;
    Library maxScoring = null;
    for (int i = 0; i < libraries.length; ++i) {
      if (!libraries[i].active && libraryScore(libraries[i], daysLeft) > max) {
        max = libraryScore(libraries[i], daysLeft);
        maxScoring = libraries[i];
      }
    }
    return maxScoring; // return max score
  }

  public static double libraryScore(Library lib, int daysLeft) {
    double score = 0;
    int booksToAdd = lib.booksPerDay * (daysLeft - lib.daysSetup);
    int booksAdded = 0;
    for (int i = 0; i < lib.books.length && booksAdded < booksToAdd; ++i) {
      if (!lib.books[i].scanned) {
        score += lib.books[i].score;
        booksAdded++;
      }
    }
    return score / daysLeft;
  }

  public static void assignBooks() {
    for (Library lib : libraries) { // For each library
      if (lib.active) { // If it is active
        int booksAdded = 0;

        for (int m = 0; m < lib.numBooks; ++m) {
          if (!lib.books[m].scanned) {
            if (assigned.get(lib.ID) == null) {
              assigned.put(lib.ID, new ArrayList<Book>());
            }
            assigned.get(lib.ID).add(lib.books[m]);
            lib.books[m].scanned = true;
            booksAdded++;

            if (booksAdded == lib.booksPerDay) {
              break;
            }
          }
        }
      }
    }
  }
}
