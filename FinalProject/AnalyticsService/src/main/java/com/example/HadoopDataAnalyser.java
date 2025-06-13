package com.example;

import com.examples.kafka.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.annotation.PostConstruct;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.spark.sql.functions.*;

@Component
public class HadoopDataAnalyser {

    @Autowired
    Producer producer;

    @PostConstruct
    void Start() throws JsonProcessingException {
        SparkConf conf = new SparkConf()
                .setAppName("Hadoop Analytics")
                .setMaster("yarn")
                .set("fs.defaultFS", "hdfs://hadoop-namenode:9000")
                .set("spark.driver.extraJavaOptions", "-Dlog4j.configurationFile=file:src/main/resources/log4j2-null.xml");

        SparkSession spark = SparkSession.builder()
                .config(conf)
                .getOrCreate();

        // Чтение данных
        Dataset<Row> requests = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .json("hdfs:///user-requests");

        // Анализ данных
        Dataset<Row> productCounts = requests.groupBy("productName").agg(count("*").alias("request_count"));;

        Dataset<Row> topProducts = productCounts.orderBy(desc("requestCount")).limit(3);

        List<String> jsonList = topProducts.toJSON().collectAsList();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (String jsonStr : jsonList) {
            arrayNode.add(mapper.readTree(jsonStr));
        }

        String finalJsonArray = mapper.writeValueAsString(arrayNode);

        spark.stop();

        producer.Produce(finalJsonArray, "products-recommendations");
    }

}
