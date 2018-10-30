package com.decipherzone.librarysystemsample.db;

import com.decipherzone.librarysystemsample.config.MongoDBConfig;
import com.decipherzone.librarysystemsample.entity.Book;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoDbOperation {

    /**
     * @return
     * @purpose - This method is used to display all the records of the books from addbook tables.
     */

    public List<Document> getAllBooks() {
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("addbooks");
            long count = collection.count();
            if (count == 0) {
                System.out.println("Books Not Available !!");
            }
            List<Document> documentList = (List<Document>) collection.find().into(new ArrayList<Document>());
            return documentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     * @purpose - This method is used to display the details of students who ordered books.
     */

    public List<Document> studentdetails() {
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookdetail");
            long count = collection.count();
            if (count == 0) {
                System.out.println("No Books Alloted !!");
                return null;
            } else {
                List<Document> documentList = (List<Document>) collection.find().into(new ArrayList<Document>());
                return documentList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * generateId method
     *
     * @return
     * @purpose - This method generate The idd of books
     */

    public int generateID() {
        int id = 0;
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("addbooks");
            long count = collection.count();
            if (count == 0) {
                System.out.println("Books Not Available !!");
                return 0;
            }
            List<Document> documentList = (List<Document>) collection.find().into(new ArrayList<Document>());
            for (Document document : documentList) {
                id = document.getInteger("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id + 1;
    }


    /**
     * @param bookquantity
     * @param originalid
     * @purpose - This method Updates the bookquantity using id in addbooks tables.
     */

    public void updateaddbook(int bookquantity, int originalid) {
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("addbooks");
            collection.updateOne(Filters.eq("id", originalid), Updates.set("quantity", bookquantity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param book
     * @purpose - thhis method is used to insert New Books records in addbook tables;
     */

    public void savebook(Book book) {
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("addbooks");
            int id = generateID();
            Document document = new Document("id", id);
            document.put("bookname", book.getBookName());
            document.put("quantity", book.getQuantity());
            collection.insertOne(document);
        } catch (Exception e) {
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
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookdetail");
            long count = collection.count();
            if (count == 0) {
                studentid = 11;
            } else {
                List<Document> documentList = (List<Document>) collection.find().into(new ArrayList<Document>());
                for (Document document : documentList) {
                    studentid = document.getInteger("studentid");
                }
                studentid = studentid + 1;
            }
            for (Book book : books) {
                Document document = new Document("id", book.getId());
                document.put("studentid", studentid);
                document.put("studentname", book.getStudentname());
                document.put("bookname", book.getBookName());
                document.put("quantity", book.getQuantity());
                document.put("issuedate", book.getIssuedate());
                document.put("returndate", book.getReturndate());
                document.put("fine", book.getFine());
                collection.insertOne(document);
            }
        } catch (Exception e) {
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

    public List<Document> Studentid(int studentid) {
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookdetail");
            List<Document> documentList = (List<Document>) collection.find(Filters.eq("studentid", studentid)).into(new ArrayList<Document>());
            return documentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookdetail");
            collection.updateOne(Filters.eq("studentid", studentid), Updates.set("returndate", returndate));
            collection.updateOne(Filters.eq("studentid", studentid), Updates.set("quantity", quantity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * datediff
     *
     * @param studentid
     * @return datediff
     * @purpose - This method calculate and return the date differenece between issuedate of book and returndate of book.
     */

    public long dateDiff(int studentid, String returndate) {
        long datediff = 0;
        String issuedate = "";
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookdetail");
            List<Document> documentList = (List<Document>) collection.find(Filters.eq("studentid", studentid)).into(new ArrayList<Document>());
            for (Document document : documentList) {
                issuedate = document.getString("issuedate");
            }
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = myFormat.parse(issuedate);
            Date returnDate = myFormat.parse(returndate);
            long difference = returnDate.getTime() - issueDate.getTime();
            datediff = (difference / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datediff;
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
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("addbooks");
            List<Document> documentList = (List<Document>) collection.find(Filters.eq("id", id)).into(new ArrayList<Document>());
            for (Document document : documentList) {
                addbookquantity = document.getInteger("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addbookquantity;
    }


    /**
     * fineUpdate method
     *
     * @param fine
     * @param studentid
     * @purpose - This method update fine in bookdetail tables;
     */
    public void fineUpdate(long fine, int studentid) {
        try {
            MongoDBConfig mongoDBConfig = new MongoDBConfig();
            MongoClient mongoClient = mongoDBConfig.getConnection();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookdetail");
            collection.updateOne(Filters.eq("studentid", studentid), Updates.set("fine", fine));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
