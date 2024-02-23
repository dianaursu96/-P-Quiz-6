package com.demo.quiz6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AdditionController {

    @GetMapping("/add/first/{first}/second/{second}")
    public String addNumbers(@PathVariable int first, @PathVariable int second) {
        int sum = first + second;
        return "The sum of " + first + " and " + second + " is: " + sum;
    }

}
