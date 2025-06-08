package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicsOptions {

    @Value("${topic-names.users-search-requests}")
    public String usersSearchRequestsTopicName;

    @Value("${topic-names.products}")
    public String productsTopicName;

    @Value("${topic-names.filtered-products}")
    public String filteredProductsTopicName;
}
