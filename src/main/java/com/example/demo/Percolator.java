// reference -> https://github.com/jdhok/diypercolate/blob/master/diypercolator/src/main/java/percolate/Percolator.java
package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.monitor.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Matches;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;

@Component
public class Percolator {
    public static final Version VERSION = Version.LUCENE_8_6_0;
    public static final String F_CONTENT = "content";
    private List<Query> queries;
    private MemoryIndex index;

    public Percolator() {
        queries = new ArrayList<Query>();
        index = new MemoryIndex();
    }

    public void addQuery(String query) throws ParseException {
        Analyzer analyzer = new SimpleAnalyzer();
        QueryParser parser = new QueryParser(F_CONTENT, analyzer);
        queries.add(parser.parse(query));
    }


    public List<Query> getMatchingQueries(String doc) {
        synchronized (index) {
            index.reset();
            index.addField(F_CONTENT, doc, new SimpleAnalyzer());
        }

        List<Query> matching = new ArrayList<Query>();
        for (Query qry : queries) {
            if (index.search(qry) > 0.0f) {
                matching.add(qry);
            }
        }

        return matching;
    }

    public String monitorQueries() throws IOException {
        Presearcher p = new TermFilteredPresearcher();
        Analyzer a = new SimpleAnalyzer();
        Monitor m = new Monitor(a, p);

        Query wildcardQuery = new WildcardQuery(new Term("first", "hello*"));
        MonitorQuery mq = new MonitorQuery("query1", wildcardQuery);
        m.register(mq);

        Document document = new Document();
        document.add(new TextField("first", "hellooooo", null));

        MatchingQueries<QueryMatch> matches = m.match(document, QueryMatch.SIMPLE_MATCHER);
        for (QueryMatch qm: matches.getMatches()){
            System.out.println(qm.toString());
            System.out.println(qm.getQueryId());
            return qm.toString();
        }
        return "NO RESULT";
    }

}