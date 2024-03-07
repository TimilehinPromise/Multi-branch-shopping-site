CREATE TABLE threshold (
                           id SERIAL PRIMARY KEY,
                           value NUMERIC NOT NULL,
                           monetary_amount NUMERIC NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);