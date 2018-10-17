import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Operation Class
 *
 * @purpose - This class main function is to add books in database and display, oreder return the books.
 * @implnote - In this class we have four methods--
 * --addbooks()
 * --displayBooks()
 * --orderBooks()
 * --returnBooks()
 */

public class Operation {
    private Connection con = null;
    private PreparedStatement preparedStatement, preparedStatement1, preparedStatement2, preparedStatement3 = null;
    private ResultSet resultSet, resultSet1, resultSet2 = null;
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private int day = 0;
    private int month = 0;
    private int year = 0;

    /**
     * addbook() Method
     *
     * @throws SQLException
     * @purpose - This Method Add the Book Details In The Database.
     * @implnote - This method takes the bookname and quantity from the user as input and generates id and update it
     * in the database.if the book name is already present in the database then the quantity of the book is added
     * with the previous quantity then updated in the database.
     */

    public void addBook() throws SQLException {
        int bookquantity = 0;
        int originalbookquantity = 0;
        int id = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?useSSL=false", "root", "root");

            System.out.println("Enter The Book Name");
            String bookname = bufferedReader.readLine().toUpperCase();
            System.out.println("Enter Quantities Of Book (quantity must be greater than 0)");

            try {
                bookquantity = Integer.parseInt(bufferedReader.readLine());
                if (bookquantity < 1) {
                    throw new UserException("Enter Correct Quantities Of Book ");
                }
            } catch (UserException u) {
                u.printStackTrace();
                return;
            } catch (NumberFormatException n) {
                n.printStackTrace();
                return;
            }

            preparedStatement = con.prepareStatement("select * from addbooks");
            preparedStatement1 = con.prepareStatement("insert into addbooks values(?,?,?);");
            resultSet = preparedStatement.executeQuery();
            boolean i = resultSet.last();
            if (!i) {
                id = 1;
            } else {
                id = resultSet.getInt(1) + 1;
            }


            resultSet1 = preparedStatement.executeQuery();
            String originalbookname = "";
            int found = 0;
            int originalid = 0;
            while (resultSet1.next()) {
                originalbookname = resultSet1.getString(2).toUpperCase();
                originalbookquantity = resultSet1.getInt(3);
                if (bookname.equals(originalbookname)) {
                    bookquantity = bookquantity + originalbookquantity;
                    preparedStatement1.setInt(3, bookquantity);
                    originalid = resultSet1.getInt(1);
                    found = 1;
                }
            }
            if (found == 1) {
                preparedStatement2 = con.prepareStatement("update addbooks set quantity=" + bookquantity + " where id=" + originalid + ";");
                preparedStatement2.executeUpdate();

                System.out.println("");
                System.out.println(bookname + " is already present and the previous quantity is added");
                System.out.println("Total Quantity : " + bookquantity);
                System.out.println("");
            } else {
                preparedStatement1.setInt(1, id);
                preparedStatement1.setString(2, bookname);
                preparedStatement1.setInt(3, bookquantity);
                preparedStatement1.executeUpdate();

                System.out.println("");
                System.out.println("Your Book With --");
                System.out.println("Id Of Book is  : " + id);
                System.out.println("Book Name      : " + bookname);
                System.out.println("Book Quantity  : " + bookquantity);
                System.out.println("Is Successfully Added");
                System.out.println("");
            }

        } catch (SQLException s) {
            s.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } finally {
            con.close();
        }
    }


    /**
     * @purpose - This method Displays The Books Available in The Database.
     */

    public void displayBooks() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?useSSL=false", "root", "root");
            preparedStatement = con.prepareStatement("Select * from addbooks");
            resultSet = preparedStatement.executeQuery();
            boolean i = resultSet.next();
            if (!i) {
                System.out.println("Table is Empty");
            }
            while (i) {
                System.out.println(resultSet.getInt(1) + "  " + resultSet.getString(2) + " " + resultSet.getInt(3));
                i = resultSet.next();
            }

        } catch (SQLException s) {
            s.printStackTrace();
        } catch (ClassNotFoundException s) {
            s.printStackTrace();
        }
    }


    /**
     * orderBooks() Method
     *
     * @throws IOException
     * @purpose - this method order books available in database
     * @implnotes - In the First, Display function will be called and then it  takes student name and book id as input
     * from user if the name and id are correct than the book will be ordered.
     */


    public void orderBooks() throws IOException {
        displayBooks();
        System.out.println("");
        String bookname = "";
        String bookoriginalname = "";
        int quantity = 0;
        int bookoriginalquantity = 0;
        String studentname = "";
        int id = 0;
        int studentid = 0;
        int dbstudentid = 0;


        System.out.println("Enter Student name");
        studentname = bufferedReader.readLine();
        System.out.println("Enter ID of The book");
        try {
            id = Integer.parseInt(bufferedReader.readLine());
        } catch (NumberFormatException n) {
            n.printStackTrace();
            return;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?useSSL=false", "root", "root");
            preparedStatement = con.prepareStatement("insert into bookdetail values(?,?,?,?,?,?,?,?);");
            preparedStatement1 = con.prepareStatement("select * from addbooks where id=" + id + ";");
            preparedStatement2 = con.prepareStatement("select * from bookdetail");
            resultSet = preparedStatement2.executeQuery();
            boolean i = resultSet.last();
            if (!i) {
                studentid = 11;
            } else {
                studentid = resultSet.getInt(2) + 1;
            }

            resultSet2 = preparedStatement1.executeQuery();
            resultSet2.next();
            bookoriginalquantity = resultSet2.getInt(3);
            bookoriginalname = resultSet2.getString(2).toUpperCase();
            System.out.println("Enter The book Name as Listed Above");
            bookname = bufferedReader.readLine().toUpperCase();
            try {

                if (!bookname.equals(bookoriginalname)) {
                    throw new UserException("Enter Correct Book NAme as Listed ");
                }
            } catch (UserException u) {
                u.printStackTrace();
                return;
            }
            if(bookoriginalquantity==0)
            {
                System.out.println("This Book Is Not Available");
                return;
            }
            System.out.println("Enter The Quantity (Quantity must be less than " + bookoriginalquantity + " and greater than 0)");
            quantity = Integer.parseInt(bufferedReader.readLine());
            try {

                if (quantity > bookoriginalquantity) {
                    throw new UserException("Enter Valid Quantity");
                } else {
                    bookoriginalquantity = bookoriginalquantity - quantity;
                }
            } catch (UserException u) {
                u.printStackTrace();
                return;
            }
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, studentid);
            preparedStatement.setString(3, studentname);
            preparedStatement.setString(4, bookname);
            preparedStatement.setInt(5, quantity);

            String date = date();
            preparedStatement.setString(6, date);
            preparedStatement.setString(7, null);
            preparedStatement.setInt(8, 0);
            preparedStatement.executeUpdate();

            preparedStatement2 = con.prepareStatement("update addbooks set quantity=" + bookoriginalquantity + " where id=" + id + ";");
            preparedStatement2.executeUpdate();

            System.out.println("");
            System.out.println("Order Details Of   " + studentname);
            System.out.println("Student Id       : " + studentid);
            System.out.println("Book Name        : " + bookname);
            System.out.println("Book Quanrity    : " + quantity);
            System.out.println("Issue Date       : " + date);
            System.out.println("If The Book is returned after \"7 days\" you will be charged 5 RS per Day.");
            System.out.println("");


        } catch (SQLException s) {
            s.printStackTrace();
        } catch (ClassNotFoundException s) {
            s.printStackTrace();
        }
    }


    /**
     * returnBooks() Method
     *
     * @purpose- this method is used to return the books which are previously ordered.
     */

    public void returnBooks() {
        String bookname = "";
        String bookoriginalname = "";
        int quantity = 0;
        int returnquantity = 0;
        System.out.println("");
        int id = 0;
        int studentid = 0;
        try {
            System.out.println("Enter the Studentid ( Starting from 11)");
            try {
                studentid = Integer.parseInt(bufferedReader.readLine());
            } catch (NumberFormatException n) {
                n.printStackTrace();
                return;
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarysystem?useSSL=false", "root", "root");
            preparedStatement = con.prepareStatement("select * from bookdetail where studentid=" + studentid + ";");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            System.out.println("Student Name :" + resultSet.getString(3));
            System.out.println("Book Name    :" + resultSet.getString(4));
            System.out.println("Quantity     :" + resultSet.getInt(5));
            System.out.println("Issue Date   :" + resultSet.getString(6));
            System.out.println("Enter the Book name you want to return");
            bookname = bufferedReader.readLine().toUpperCase();
            bookoriginalname = resultSet.getString(4).toUpperCase();
            try {

                if (!bookname.equals(bookoriginalname)) {
                    throw new UserException("Enter Correct Book NAme as Listed ");

                }
            } catch (UserException u) {
                u.printStackTrace();
                return;
            }

            quantity = resultSet.getInt(5);
            System.out.println("Enter the Quantity of books you want to return (you have total " + quantity + " books)");
            try {
                returnquantity = Integer.parseInt(bufferedReader.readLine());

            } catch (NumberFormatException n) {
                n.printStackTrace();
                return;
            }
            if (returnquantity > quantity) {
                System.out.println("Invalid Quantity !! Enter valid Quantity");
                return;
            } else {
                quantity = quantity - returnquantity;
            }

            String issuedate = resultSet.getString(6);
            int year = Integer.parseInt(issuedate.substring(0, 4));
            int month = Integer.parseInt(issuedate.substring(5, 7));
            int day = Integer.parseInt(issuedate.substring(8));
            System.out.println("Enter return Year(must be greater than or equal to " + year + ")");
            int returnyear = 0;
            try {
                returnyear = Integer.parseInt(bufferedReader.readLine());

            } catch (NumberFormatException n) {
                n.printStackTrace();
                return;
            }
            if (returnyear < year) {
                System.out.println("invalid year");
                return;
            }


            int returnmonth = 0;
            if (year < returnyear) {
                System.out.println("Enter return Month");

                try {
                    returnmonth = Integer.parseInt(bufferedReader.readLine());

                } catch (NumberFormatException n) {
                    n.printStackTrace();
                    return;
                }
                if (returnmonth > 12) {
                    System.out.println("invalid month");
                    return;
                }

            } else {
                System.out.println("Enter return month(must be greater than or equal to " + month + ")");
                try {
                    returnmonth = Integer.parseInt(bufferedReader.readLine());

                } catch (NumberFormatException n) {
                    n.printStackTrace();
                    return;
                }
                if (returnmonth < month) {
                    System.out.println("invalid month");
                    return;
                } else if (returnmonth > 12) {
                    System.out.println("invalid month");
                    return;
                }

            }


            int returnday = 0;
            if (month < returnmonth) {
                System.out.println("Enter return day");
                try {
                    returnday = Integer.parseInt(bufferedReader.readLine());

                } catch (NumberFormatException n) {
                    n.printStackTrace();
                    return;
                }

                if (returnday > 30) {
                    System.out.println("invalid day");
                    return;
                }
            } else {
                System.out.println("Enter return day(must be greater than or equal to " + day + ")");
                try {
                    returnday = Integer.parseInt(bufferedReader.readLine());

                } catch (NumberFormatException n) {
                    n.printStackTrace();
                    return;
                }
                if (returnday < day) {
                    System.out.println("invalid Day");
                    return;
                } else if (returnday > 30) {
                    System.out.println("invalid day");
                    return;
                }

            }
            String returndate = returnyear + "-" + returnmonth + "-" + returnday;
            preparedStatement1 = con.prepareStatement("update bookdetail set returndate=" + "'" + returndate + "', quantity=" + quantity + " where studentid=" + studentid + ";");
            preparedStatement1.executeUpdate();
            preparedStatement2 = con.prepareStatement("select DATEDIFF(returndate,issuedate) from bookdetail where studentid=" + studentid + ";");
            resultSet1 = preparedStatement2.executeQuery();
            resultSet1.next();
            preparedStatement3 = con.prepareStatement("select quantity from addbooks where id=" + id + ";");
            resultSet2 = preparedStatement3.executeQuery();
            resultSet2.next();
            int addbookquantity = resultSet2.getInt(1);

            addbookquantity = returnquantity + addbookquantity;

            preparedStatement2 = con.prepareStatement("update addbooks set quantity=" + addbookquantity + " where id=" + id + ";");
            preparedStatement2.executeUpdate();
            int fine = 0;
            if (resultSet1.getInt(1) > 7) {
                fine = (resultSet1.getInt(1) - 7) * 5;
                fine = fine + resultSet.getInt(8);
                System.out.println("Your Fine for " + (resultSet1.getInt(1) - 7) + " days is " + (resultSet1.getInt(1) - 7) * 5);
                preparedStatement2 = con.prepareStatement("update bookdetail set fine=" + fine + " where studentid=" + studentid + ";");
                preparedStatement2.executeUpdate();
                System.out.println("Your Book Is returned Successfully And the Quantity You Return is " + returnquantity);
            }
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (SQLException s) {
            s.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }


    /**
     * date method
     *
     * @return date
     * @purpose- This method returns current system date.
     */

    public String date() {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        month += 1;
        return year + "-" + month + "-" + day;
    }
}