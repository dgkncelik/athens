package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    private Percolator percolator;

    @Autowired
    public MainController(Percolator percolator){
        this.percolator = percolator;
    }

    @PostMapping("/add_query")
    public String addQuery(@RequestBody AthensQuery athensQuery){
        this.percolator.addQuery(athensQuery);
        return "OK";
    }

    @GetMapping("/percolate")
    public List<AthensQuery> percolate(@PathParam("text") String text){
        return this.percolator.percolateText(text);
    }
}


