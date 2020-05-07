package Controllers;

import Models.Book;
import Server.Application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookService
{

    /* Different models will require different read and write methods. Here is an example 'loadAll' method 
     * which is passed the target list object to populate. */
    public static void readAll(List<Book> list) {
        list.clear();       // Clear the target list first.

        try{
            PreparedStatement statement = Application.db.prepareStatement("SELECT BookID, Title, AuthorID FROM Books ORDER BY BookID");
            if (statement != null)      // Assuming the statement correctly initated...
            {
                ResultSet results = statement.executeQuery();       // ...run the query!
                System.out.println(results.getString(2));
                if (results != null)        // If some results are returned from the query...
                {
                    // ...add each one to the list.
                        while (results.next()) {
                            list.add( new Book(results.getInt("BookID"), results.getString("Title"), results.getInt("AuthorID")));
                        }
                    }
                }

        }catch(SQLException se){
            System.out.println("Database result processing error: " + se.getMessage());
        }
    }

    public static Book getById(int BookID){
        Book book = null;
        try{
            PreparedStatement statement = Application.db.prepareStatement("SELECT BookID, Title, AuthorID FROM Books WHERE BookID = ?");
            if (statement != null)
            {
                statement.setInt(1, BookID);
                ResultSet results = statement.executeQuery();

                if (results != null)
                {
                    book = new Book(results.getInt("BookID"), results.getString("Title"), results.getInt("AuthorID"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

        return book;
    }

    public static void deleteById(int BookID)
    {
        try{

            PreparedStatement statement = Application.db.prepareStatement("DELETE FROM Books WHERE BookID = ?");
            statement.setInt(1, BookID);

            statement.executeUpdate();
        } catch (SQLException resultsexception){
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }
    }

    public static void save(Book book){

        try{
            PreparedStatement statement = Application.db.prepareStatement("INSERT INTO Books (Title, AuthorID) VALUES (?, ?)");
            statement.setString(1, book.Title);
            statement.setInt(2, book.AuthorID);

            statement.executeUpdate();

        }
        catch (SQLException resultsexception) {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }
    }


}
