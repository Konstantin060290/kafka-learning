package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductsConsumerOptions {
    @Value("${client-api.consumer.group-id:consumer-group-0}")
    public String consumerGroupId;

    @Value("${client-api.consumer.auto-offset-reset:earliest}")
    public String autoOffsetReset;

    @Value("${client-api.consumer.enable-auto-commit:true}")
    public String enableAutoCommit;

    @Value("${client-api.consumer.session-timeout:6000}")
    public String sessionTimeOut;

    @Value("${client-api.consumer.login:consumer}")
    public String consumerLogin;

    @Value("${client-api.consumer.password:consumer-secret}")
    public String consumerPwd;
}
