package com.decipherzone.librarysystemsample;

import com.decipherzone.librarysystemsample.db.MongoDbOperation;
import com.decipherzone.librarysystemsample.entity.Book;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * com.decipherzone.librarysystemsample.MongoOperation Class
 *
 * @purpose - This class main function is to add books in database and display, oreder return the books.
 * @implnote - In this class we have four methods--
 * --addbooks()
 * --displayBooks()
 * --orderBooks()
 * --returnBooks()
 */

public class MongoOperation {
    MongoDbOperation mongoDbOperation = new MongoDbOperation();
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private static int returnvalue = 0;

    /**
     * addbook() Method
     *
     * @purpose - This Method Add the Book Details In The Database.
     * @implnote - This method takes the bookname and quantity from the user as input and generates id and update it
     * in the database.if the book name is already present in the database then the quantity of the book is added
     * with the previous quantity then updated in the database.
     */

    public void addBook() {
        int bookquantity = 0;
        int originalbookquantity = 0;
        int id = 0;
        String originalbookname = "";
        int found = 0;
        int originalid = 0;
        Book book = new Book();
        try {
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
            List<Document> bookList = mongoDbOperation.getAllBooks();
            if (bookList.isEmpty()) {
                found = 0;
            } else {
                for (Document document : bookList) {
                    originalbookname = document.getString("bookname").toUpperCase();
                    originalbookquantity = document.getInteger("quantity");
                    if (bookname.equals(originalbookname)) {
                        bookquantity = bookquantity + originalbookquantity;
                        originalid = document.getInteger("id");
                        found = 1;
                    }
                }
            }
            id = mongoDbOperation.generateID();
            book.setId(id);
            book.setBookName(bookname);
            book.setQuantity(bookquantity);
            if (found == 1) {
                mongoDbOperation.updateaddbook(bookquantity, originalid);
                System.out.println("");
                System.out.println(bookname + " is already present and the previous quantity is added");
                System.out.println("Total Quantity : " + bookquantity);
                System.out.println("");
            } else {
                mongoDbOperation.savebook(book);
                System.out.println("");
                System.out.println("Your Book With --");
                System.out.println("Id Of Book is  : " + id);
                System.out.println("Book Name      : " + bookname);
                System.out.println("Book Quantity  : " + bookquantity);
                System.out.println("Is Successfully Added");
                System.out.println("");
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
    }

    /**
     * @purpose - This method Displays The Books Available in The Database.
     */

    public void displayBooks() {
        try {
            List<Document> bookList = mongoDbOperation.getAllBooks();
            System.out.println("Id|   Book Name      |   Quantity ");
            for (Document d : bookList) {
                System.out.println(d.getInteger("id") + " " + d.getString("bookname") + " " + d.getInteger("quantity"));
            }
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * displayBooksOrderreturn() method
     *
     * @purpose - This method is displays the details of students who have books.
     */

    public void displayBooksOrderreturn() {
        try {
            List<Document> bookList = mongoDbOperation.studentdetails();
            if (bookList == null) {
                returnvalue = 11;
                return;
            } else {
                System.out.println("");
                System.out.println("Id | StudentId |    Student NAme    |          Book NAme      | Quantity | issuedate |    returndate    |   Fine");
                for (Document document : bookList) {
                    System.out.println(document.getInteger("id") + "   " + document.getInteger("studentid") + "                " + document.getString("studentname") + "                  " + document.getString("bookname") + "         " + document.getInteger("quantity") + "        " + document.getString("issuedate") + "        " + document.getString("returndate") + "      " + document.getLong("fine"));
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


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

        System.out.println("Enter Student name");
        studentname = bufferedReader.readLine();
        if (studentname.length() < 3) {
            System.out.println("Input valid name !!");
            return;
        }
        System.out.println("Enter ID of The book");
        try {
            id = Integer.parseInt(bufferedReader.readLine());
        } catch (NumberFormatException n) {
            n.printStackTrace();
            return;
        }
        List<Document> bookList = mongoDbOperation.getAllBooks();
        for (Document document : bookList) {
            if (document.getInteger("id") == id) {
                bookoriginalquantity = document.getInteger("quantity");
                bookoriginalname = document.getString("bookname").toUpperCase();
            }
        }
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
        if (bookoriginalquantity == 0) {
            System.out.println("This Book Is Not Available");
            return;
        }
        System.out.println("Enter The Quantity (Quantity must be less than " + bookoriginalquantity + " and greater than 0)");
        try {
            quantity = Integer.parseInt(bufferedReader.readLine());
        } catch (NumberFormatException n) {
            n.printStackTrace();
            return;
        }
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
        if (quantity <= 0) {
            System.out.println("Enter valid Quantity");
            return;
        }
        String date = date();
        List<Book> bookList1 = new ArrayList<Book>();
        Book book = new Book(id, studentid, studentname, bookname, quantity, date, null, 0);
        bookList1.add(book);
        mongoDbOperation.orderbooksdb(bookList1);
        mongoDbOperation.updateaddbook(bookoriginalquantity, id);
        System.out.println("");
        System.out.println("Order Details Of   " + studentname);
        System.out.println("Book Name        : " + bookname);
        System.out.println("Book Quanrity    : " + quantity);
        System.out.println("Issue Date       : " + date);
        System.out.println("If The Book is returned after \"7 days\" you will be charged 5 RS per Day.");
        System.out.println("");
    }


    /**
     * returnBooks() Method
     *
     * @purpose- this method is used to return the books which are previously ordered.
     */

    public void returnBooks() {
        displayBooksOrderreturn();
        String bookname = "";
        String bookoriginalname = "";
        int quantity = 0;
        int returnquantity = 0;
        int id = 0;
        int studentid = 0;
        String issuedate = "";
        long dbfine = 0;
        try {
            if (returnvalue == 11) {
                return;
            } else {

                System.out.println("Enter the Studentid ( Starting from 11)");
                try {
                    studentid = Integer.parseInt(bufferedReader.readLine());
                } catch (NumberFormatException n) {
                    n.printStackTrace();
                    return;
                }
                List<Document> bookList = mongoDbOperation.Studentid(studentid);
                for (Document document : bookList) {
                    id = document.getInteger("id");
                    System.out.println("Student Name :" + document.getString("studentname"));
                    System.out.println("Book Name    :" + document.getString("bookname"));
                    System.out.println("Quantity     :" + document.getInteger("quantity"));
                    System.out.println("Issue Date   :" + document.getString("issuedate"));
                    quantity = document.getInteger("quantity");
                    issuedate = document.getString("issuedate");
                    bookoriginalname = document.getString("bookname").toUpperCase();
                    dbfine = document.getLong("fine");
                }
                System.out.println("Enter the Book name you want to return");
                bookname = bufferedReader.readLine().toUpperCase();
                try {
                    if (!bookname.equals(bookoriginalname)) {
                        throw new UserException("Enter Correct Book NAme as Listed ");

                    }
                } catch (UserException u) {
                    u.printStackTrace();
                    return;
                }

                if (quantity == 0) {
                    System.out.println("");
                    System.out.println("\"All The books are retured\"");
                    System.out.println("");
                    return;
                }
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
                mongoDbOperation.returnDateandQuantityUpdate(returndate, quantity, studentid);
                long datediff = mongoDbOperation.dateDiff(studentid, returndate);
                int addbookquantity = mongoDbOperation.addbookQuantity(id);
                addbookquantity = returnquantity + addbookquantity;
                mongoDbOperation.updateaddbook(addbookquantity, id);
                long fine = 0;
                if (datediff > 7) {
                    fine = (datediff - 7) * 5;
                    fine = fine + dbfine;
                    System.out.println("Your Fine for " + (datediff - 7) + " days is " + (datediff - 7) * 5);
                    mongoDbOperation.fineUpdate(fine, studentid);
                    System.out.println("Your Book Is returned Successfully And the Quantity You Return is " + returnquantity);
                }
            }
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
