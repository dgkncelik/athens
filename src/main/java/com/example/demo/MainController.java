package com.example.demo;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Percolator;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class MainController {


    private Percolator percolator;

    @Autowired
    public MainController(Percolator percolator){
        this.percolator = percolator;
    }

    @GetMapping("/add_query")
    public String addQuery(@PathParam("query") String query){
        try {
            this.percolator.addQuery(query);
        }catch (ParseException e){
            return "Error on parsing query";
        }
        return "OK";
    }

    @GetMapping("/percolate")
    public String percolateDocument(@PathParam("document") String document){
        String results = "";
        results = document + " -> " + percolator.getMatchingQueries(document);
        return results;
    }

    @GetMapping("/hello")
    public String hello(@PathParam("name") String name){
        return "hello " + name;
    }

    @GetMapping("/test_percolator")
    public String percolator_test() throws ParseException {
        Percolator percolator = new Percolator();
        percolator.addQuery("one");
        percolator.addQuery("two");
        percolator.addQuery("three");

        String  docs[] = {
                "I have two or three apples",
                "i have no apples"
        };

        StringBuilder results = new StringBuilder();
        for (String doc : docs) {
            results.append(doc).append(" -> ").append(percolator.getMatchingQueries(doc));
        }
        return results.toString();
    }


}


