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

-- Create orders table
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        sku_id VARCHAR(255),
                        customer_name VARCHAR(255),
                        amount NUMERIC,
                        customer_address TEXT,
                        customer_phone VARCHAR(255),
                        status VARCHAR(255),
                        user_id BIGINT,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_user_orders FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Additional scripts to populate roles and authorities if needed
INSERT INTO role (name) VALUES ('CUSTOMER'), ('ADMIN');
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





