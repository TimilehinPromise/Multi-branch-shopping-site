CREATE TABLE delivery_areas (
                                id SERIAL PRIMARY KEY,
                                area_name VARCHAR(255) UNIQUE NOT NULL,
                                delivery_price NUMERIC(19, 4) NOT NULL
);
