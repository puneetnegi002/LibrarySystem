package com.decipherzone.librarysystemsample.db;

import com.decipherzone.librarysystemsample.Operation;
import com.decipherzone.librarysystemsample.config.DBConfig;
import com.decipherzone.librarysystemsample.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DbOperations Class
 *
 * @purpose - This class only perform database operations.
 */

public class DbOperations {


    /**
     * generateId method
     *
     * @return
     * @purpose - This method generate The idd of books
     */
    public static int generateID() {
        int id = 0;
        try {
            Connection connection = DBConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from addbooks");
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean i = resultSet.last();
            if (!i) {
                id = 1;
            } else {
                id = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * @return
     * @purpose - This method is used to display all the records of the books from addbook tables.
     */

    public static List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<Book>();
        try {
            Connection connection = DBConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from addbooks");
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean i = resultSet.next();
            if (!i) {
                System.out.println("Books Not Available !!");
                return null;
            }
            ResultSet resultSet1 = preparedStatement.executeQuery();
            while (resultSet1.next()) {
                Book book = new Book();
                book.setId(resultSet1.getInt(1));
                book.setBookName(resultSet1.getString(2).toUpperCase());
                book.setQuantity(resultSet1.getInt(3));
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    /**
     * @return
     * @purpose - This method is used to display the details of students who ordered books.
     */

    public static List<Book> studentdetails() {
        List<Book> bookList = new ArrayList<Book>();
        try {
            Connection connection = DBConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from bookdetail");
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean i = resultSet.next();
            if (!i) {
                System.out.println("No Books Alloted !!");
                return null;
            }
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8));
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;

    }


    /**
     * @param book
     * @purpose - thhis method is used to insert New Books records in addbook tables;
     */

    public static void savebook(Book book) {
        try {
            Connection connection = DBConfig.getConnection();
            int id = generateID();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into addbooks values(?,?,?);");
            preparedStatement.setInt(1, book.getId());
            preparedStatement.setString(2, book.getBookName());
            preparedStatement.setInt(3, book.getQuantity());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * @param bookquantity
     * @param originalid
     * @purpose - This method Updates the bookquantity using id in addbooks tables.
     */

    public void updateaddbook(int bookquantity, int originalid) {
        try {
            Connection connection = DBConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update addbooks set quantity=" + bookquantity + " where id=" + originalid + ";");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * orderbooksdb
     *
     * @param books
     * @purpose - This method is used to insert the values into bookdetails table
     */

    public void orderbooksdb(List<Book> books) {
        int studentid = 0;
        try {
            Operation operation = new Operation();
            Connection connection = DBConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from bookdetail");
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean dbcheck = resultSet.last();
            if (!dbcheck) {
                studentid = 11;
            } else {
                studentid = resultSet.getInt(2) + 1;
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("insert into bookdetail values(?,?,?,?,?,?,?,?);");
            for (Book book : books) {
                preparedStatement1.setInt(1, book.getId());
                preparedStatement1.setInt(2, studentid);
                preparedStatement1.setString(3, book.getStudentname());
                preparedStatement1.setString(4, book.getBookName());
                preparedStatement1.setInt(5, book.getQuantity());
                preparedStatement1.setString(6, book.getIssuedate());
                preparedStatement1.setString(7, book.getReturndate());
                preparedStatement1.setInt(8, book.getFine());
            }
            preparedStatement1.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Studentit Method
     *
     * @param studentid
     * @return booklist
     * @purpose - this method is used to get the details of a specific student
     */

    public List<Book> Studentid(int studentid) {
        List<Book> booklist = new ArrayList<Book>();
        Connection connection = DBConfig.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from bookdetail where studentid=" + studentid + ";");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Book book = new Book(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8));
            booklist.add(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booklist;
    }


    /**
     * datediff
     *
     * @param studentid
     * @return datediff
     * @purpose - This method calculate and return the date differenece between issuedate of book and returndate of book.
     */

    public int dateDiff(int studentid) {
        int datediff = 0;
        Connection connection = DBConfig.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select DATEDIFF(returndate,issuedate) from bookdetail where studentid=" + studentid + ";");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            datediff = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datediff;
    }

    /**
     * fineUpdate method
     *
     * @param fine
     * @param studentid
     * @purpose - This method update fine in bookdetail tables;
     */
    public void fineUpdate(int fine, int studentid) {
        Connection connection = DBConfig.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update bookdetail set fine=" + fine + " where studentid=" + studentid + ";");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * returnDateandQuantityUpdate Method
     *
     * @param returndate
     * @param quantity
     * @param studentid
     * @purpose - this method receive three arguments "returndate,quantity,studentid" and set these values in the bookdetail tables.
     */
    public void returnDateandQuantityUpdate(String returndate, int quantity, int studentid) {
        Connection connection = DBConfig.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update bookdetail set returndate=" + "'" + returndate + "', quantity=" + quantity + " where studentid=" + studentid + ";");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Addbookquantity method
     *
     * @param id
     * @return addbookquantity
     * @purpose -this method receives id and return the quantity from addbook table
     */
    public int addbookQuantity(int id) {
        int addbookquantity = 0;
        Connection connection = DBConfig.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select quantity from addbooks where id=" + id + ";");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            addbookquantity = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addbookquantity;
    }

}
