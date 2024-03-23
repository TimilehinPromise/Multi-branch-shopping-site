ALTER TABLE orders
    ADD COLUMN payment_id BIGINT ;

ALTER TABLE payment
    ADD COLUMN order_id BIGINT ;

ALTER TABLE payment
    ALTER COLUMN provider_response TYPE VARCHAR(1500)


