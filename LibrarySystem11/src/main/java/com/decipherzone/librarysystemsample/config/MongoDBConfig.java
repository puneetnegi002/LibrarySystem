package com.decipherzone.librarysystemsample.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;


import java.util.Arrays;
import java.util.List;

public class MongoDBConfig {

    public  MongoClient getConnection() {
            MongoCredential credential = MongoCredential.createCredential("adminuser", "admin", "root".toCharArray());
        MongoClient mongoClient = new MongoClient( new ServerAddress("localhost",27017), Arrays.asList(credential));
            System.out.println("Connected to the database successfully");


        return mongoClient;
    }
}
