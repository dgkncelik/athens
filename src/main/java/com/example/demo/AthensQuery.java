package com.example.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.monitor.Monitor;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AthensQuery {
    private String queryString; // "hello* world*"

    public String getQueryString(){
        return this.queryString;
    }

    public void setQueryString(String queryString){
        this.queryString = queryString;
    }

    public String generateQueryId(){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(this.queryString));
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (NoSuchAlgorithmException e){
            return null;
        }
    }

    public Query generateQueryObject(){
        try {
            Analyzer sa = new SimpleAnalyzer();
            QueryParser qp = new QueryParser("query", sa);
            qp.setAllowLeadingWildcard(true);
            return qp.parse(this.queryString);
        } catch (ParseException p){
            return null;
        }
    }
}
