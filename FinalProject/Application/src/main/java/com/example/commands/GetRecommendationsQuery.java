package com.example.commands;

import com.example.repositories.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GetRecommendationsQuery {

    @Autowired
    RecommendationsRepository recommendationsRepository;

    public void GetRecommendations()
    {
        var recommendations = recommendationsRepository.findAll();

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found");
        } else {
            System.out.println("Recommendations:");
            recommendations.forEach(r -> System.out.println(
                            "Product-name: " + r.getName() +"\n"
            ));
        }
    }
}
