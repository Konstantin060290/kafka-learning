CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    product_id BIGINT,
    name VARCHAR(255) NOT NULL,
    rest_information VARCHAR(5000)
);