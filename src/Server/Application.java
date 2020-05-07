package Server;

import Controllers.AuthorService;
import Controllers.BookService;
import Models.Author;
import Models.Book;
import Server.DatabaseConnection;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;

public class Application
{
    /* Our app needs a connection to the database, and this is all handled by the DatabaseConnection
     * class. A public static variable, called database, points to the DatabaseConnection object. */
    public static Connection db = null;

    public static void main(String args[]) throws SQLException {
        openDatabase("Inventory.db");

        ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);

        String cont = "Yes";

        while (cont == "Yes") {
            Scanner input = new Scanner(System.in);
            System.out.println("1. View all Books");
            System.out.println("2. View all Authors");
            System.out.println("3. Add Book");
            System.out.println("4. Add Author");
            System.out.println("5. Delete Book");
            System.out.println("6. Delete Author");
            System.out.println("7. Terminate");
            System.out.println("Key in choice");
            int choice = input.nextInt();
            input.nextLine();

            if (choice==1){
                viewallBooks();
            }
            else if (choice==2){
                viewallAuthors();
            }
            else if (choice==3){
                addBook();
            }
            else if (choice==4){
                addAuthor();
            }
            else if (choice==5){
                deleteBook();
            }
            else if (choice==6){
                deleteAuthor();
            }
            else {
                System.out.println("Bye!");
                terminate();
            }
        }
    }

    public static void viewallBooks() throws SQLException {
        ArrayList<Book> allTheBooks = new ArrayList<>();

        BookService.readAll(allTheBooks);

        for(Book b : allTheBooks) {
            System.out.println(b);
        }
    }

    public static void viewallAuthors() throws SQLException {
        ArrayList<Author> allTheAuthors = new ArrayList<>();

        AuthorService.readAll(allTheAuthors);

        for(Author a : allTheAuthors) {
            System.out.println(a);
        }
    }

    public static void addBook(){
        Scanner input = new Scanner(System.in);
        int BookID=0;
        System.out.println("Enter the name of the book:");
        String BookName = input.nextLine();
        input.nextLine();
        System.out.println("Enter the Author ID");
        int AuthorID = input.nextInt();
        input.nextLine();

        Book newBook = new Book(BookID, BookName, AuthorID);
        BookService.save(newBook);
    }

    public static void addAuthor(){
        Scanner input = new Scanner(System.in);
        int AuthorID =0;
        System.out.println("Enter author First Name:");
        String FirstName = input.nextLine();
        System.out.println("Enter author Second Name:");
        String SecondName = input.nextLine();

        Author newAuthor = new Author(AuthorID, FirstName, SecondName);
        AuthorService.save(newAuthor);
    }

    public static void deleteBook(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter book ID to delete:");
        int BookID = input.nextInt();

        BookService.deleteById(BookID);
    }

    public static void deleteAuthor(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter author ID to delete:");
        int AuthorID = input.nextInt();

        AuthorService.deleteById(AuthorID);
    }

    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    public static void terminate()
    {
        System.out.println("Closing database connection and terminating application...");
        if (db != null){
            closeDatabase();
        }
        System.exit(0);                                 // Finally, terminate the entire application.
    }

    private static void closeDatabase(){
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }
}