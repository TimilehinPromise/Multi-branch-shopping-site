-- Filename: V1__Initial_schema.sql

-- Create roles table
CREATE TABLE role (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create authorities table
CREATE TABLE authorities (
                             id BIGSERIAL PRIMARY KEY,
                             authority VARCHAR(255) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       branch_id INT NOT NULL,
                       royalty_code VARCHAR(255) UNIQUE,
                       phone VARCHAR(255),
                       email_verified BOOLEAN DEFAULT FALSE,
                       retries INT DEFAULT 0,
                       role_id BIGINT,
                       enabled BOOLEAN DEFAULT FALSE,
                       deleted BOOLEAN DEFAULT FALSE,
                       activated BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id)
);

-- Create user_authority table
CREATE TABLE user_authority (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT,
                                authority_id BIGINT,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
                                CONSTRAINT fk_authority FOREIGN KEY (authority_id) REFERENCES authorities (id)
);


-- Additional scripts to populate roles and authorities if needed
INSERT INTO role (name) VALUES ('CUSTOMER'), ('STAFF'), ('SUPER-ADMIN');
-- INSERT INTO authorities (authority) VALUES ('READ_PRIVILEGE'), ('WRITE_PRIVILEGE');

CREATE TABLE token_store (
                             id BIGSERIAL PRIMARY KEY,
                             token VARCHAR(255) NOT NULL,
                             expired_at TIMESTAMP NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT token_store_token_key UNIQUE (token)
);

CREATE TABLE address (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT,
                         name VARCHAR(225),
                         street VARCHAR(255),
                         preferred BOOLEAN DEFAULT FALSE,
                         city VARCHAR(255),
                         landmark VARCHAR(255),
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE branch (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        location VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE business_category (
                                   id SERIAL PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   is_deleted BOOLEAN NOT NULL,
                                   photo VARCHAR(255),
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE business_subcategory (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      category_id INT NOT NULL,
                                      is_deleted BOOLEAN NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (category_id) REFERENCES business_category (id)
);


CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         sku_id VARCHAR(255) NOT NULL UNIQUE,
                         description TEXT NOT NULL,
                         brand VARCHAR(255),
                         price NUMERIC NOT NULL,
                         deleted BOOLEAN NOT NULL,
                         enabled BOOLEAN NOT NULL,
                         sub_category_id BIGINT,
                         category_id BIGINT,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (sub_category_id) REFERENCES business_subcategory (id),
                         FOREIGN KEY (category_id) REFERENCES business_category (id)
);



CREATE TABLE product_branch (
                                product_id BIGINT NOT NULL,
                                branch_id BIGINT NOT NULL,
                                PRIMARY KEY (product_id, branch_id),
                                FOREIGN KEY (product_id) REFERENCES product (id),
                                FOREIGN KEY (branch_id) REFERENCES branch (id)
);

CREATE TABLE product_images (
                                id BIGSERIAL PRIMARY KEY,
                                product_id BIGINT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (product_id) REFERENCES product (id)
);
-- Create the 'loyalty' table
CREATE TABLE loyalty (
                         id BIGSERIAL PRIMARY KEY,
                         count BIGINT NOT NULL,
                         discount_value DECIMAL NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create the 'wallet' table
CREATE TABLE wallet (
                        id BIGSERIAL PRIMARY KEY,
                        amount DECIMAL NOT NULL,
                        count BIGINT NOT NULL,
                        user_id BIGINT UNIQUE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Update the 'users' table to include the 'wallet_id' column
ALTER TABLE users
    ADD COLUMN wallet_id BIGINT UNIQUE,
ADD CONSTRAINT fk_wallet
FOREIGN KEY (wallet_id) REFERENCES wallet(id);


-- Create the 'cart' table
CREATE TABLE cart (
                      id BIGSERIAL PRIMARY KEY,
                      product_id BIGINT,
                      user_id BIGINT NOT NULL,
                      price DECIMAL NOT NULL,
                      quantity INTEGER NOT NULL,
                      created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (product_id) REFERENCES product(id),
                      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        amount DECIMAL NOT NULL,
                        discounted_amount DECIMAL NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        user_id BIGINT,
                        payment_provider VARCHAR(100),
                        details TEXT,
                        from_checkout BOOLEAN NOT NULL DEFAULT TRUE,
                        message VARCHAR(255),
                        product VARCHAR(255),
                        branch_id BIGINT NOT NULL,
                        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE payment (
                         reference_id VARCHAR(255) NOT NULL,
                         user_id VARCHAR(255) NOT NULL,
                         amount DECIMAL NOT NULL,
                         status VARCHAR(255) NOT NULL,
                         provider VARCHAR(255) NOT NULL,
                         provider_response VARCHAR(255),
                         created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (reference_id, user_id)
);










