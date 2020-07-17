// reference -> https://github.com/jdhok/diypercolate/blob/master/diypercolator/src/main/java/percolate/Percolator.java
package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
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

}