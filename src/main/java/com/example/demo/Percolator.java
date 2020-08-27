// reference -> https://github.com/jdhok/diypercolate/blob/master/diypercolator/src/main/java/percolate/Percolator.java
package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.monitor.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;

@Component
public class Percolator {
    public static final Version VERSION = Version.LUCENE_8_6_0;
    public static final String F_CONTENT = "content";
    private HashMap<String, AthensQuery> queries;
    private Presearcher p;
    private Analyzer a;
    private Monitor m;

    public Percolator() throws IOException{
        this.queries = new HashMap<>();
        this.p = new TermFilteredPresearcher();
        this.a = new SimpleAnalyzer();
        this.m = new Monitor(a, p);
    }

    public void addQuery(AthensQuery athensQuery){
        String queryId = athensQuery.generateQueryId();
        Query queryObject = athensQuery.generateQueryObject();
        this.queries.put(queryId, athensQuery);
        MonitorQuery mq =  new MonitorQuery(queryId,
                                            queryObject);
        try{
            this.m.register(mq);
        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    public List<AthensQuery> percolateText(String text){
        List<AthensQuery> results = new ArrayList<>();
        MatchingQueries<QueryMatch> matches;
        Document document = new Document();
        document.add(new TextField("query", text, null));
        try {
            matches = this.m.match(document, QueryMatch.SIMPLE_MATCHER);
        } catch (IOException i){
            return results;
        }
        for (QueryMatch qm: matches.getMatches()){
            String queryId = qm.getQueryId();
            AthensQuery r = this.queries.get(queryId);
            results.add(r);
        }
        return results;
    }
}