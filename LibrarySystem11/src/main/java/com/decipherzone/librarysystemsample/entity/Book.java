package com.decipherzone.librarysystemsample.entity;

public class Book {

    private int id;
    private String bookName;
    private int quantity;
    private int studentid;
    private String studentname;
    private String issuedate;
    private String returndate;
    private int fine;


    public Book() {
        int id;
        String bookname;
        int quantity;
    }

    public Book(int id, int studentid, String studentname, String bookName, int quantity, String issuedate, String returndate, int fine) {
        this.id = id;
        this.bookName = bookName;
        this.quantity = quantity;
        this.studentid = studentid;
        this.studentname = studentname;
        this.issuedate = issuedate;
        this.returndate = returndate;
        this.fine = fine;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public String setBookName(String bookName) {
        this.bookName = bookName;
        return bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int setQuantity(int quantity) {
        this.quantity = quantity;
        return quantity;
    }

    @Override
    public String toString() {
        return id + " " + bookName + " " + quantity;
    }
}
