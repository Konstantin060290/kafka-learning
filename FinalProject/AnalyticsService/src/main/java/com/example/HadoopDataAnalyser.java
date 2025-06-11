package com.example;

import jakarta.annotation.PostConstruct;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.sum;

@Component
public class HadoopDataAnalyser {

    @PostConstruct
    void Start()
    {
        SparkConf conf = new SparkConf()
                .setAppName("Hadoop Analytics with Java")
                .setMaster("yarn") // или "local" для локального режима
                .set("fs.defaultFS", "hdfs://172.29.43.99:9000")
                .set("spark.driver.extraJavaOptions", "-Dlog4j.configurationFile=file:src/main/resources/log4j2-null.xml");

        // Создание SparkSession
        SparkSession spark = SparkSession.builder()
                .config(conf)
                //.config("spark.driver.extraJavaOptions", "-Dlog4j.configuration=file:log4j-off.properties")
                //.config("spark.executor.extraJavaOptions", "-Dlog4j.configuration=file:log4j-off.properties")
                .getOrCreate();

        //JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        // Чтение данных
        Dataset<Row> sales = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .json("hdfs:///user-requests");

        // Анализ данных
        Dataset<Row> salesByRegion = sales.groupBy("region")
                .agg(
                        sum("amount").alias("total_sales"),
                        count("transaction_id").alias("num_transactions")
                );

        // Сохранение результатов
        salesByRegion.write()
                .parquet("hdfs://results/sales_by_region.parquet");

        // Вывод результатов
        salesByRegion.show();

        spark.stop();
    }

}
