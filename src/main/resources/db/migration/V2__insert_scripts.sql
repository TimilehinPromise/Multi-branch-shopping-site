INSERT INTO business_category (name, is_deleted) VALUES
                                                     ('Autocare and Accessories', FALSE),
                                                     ('Baby and Kids', FALSE),
                                                     ('Food and Grocery', FALSE),
                                                     ('Health and Beauty', FALSE),
                                                     ('Home and Living', FALSE),
                                                     ('Electronics and Gadgets', FALSE),
                                                     ('Fashion', FALSE),
                                                     ('Stationery and Books', FALSE),
                                                     ('Food and Spirits', FALSE);


-- Autocare & Accessories Subcategories (assuming category_id = 1)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Car Accessories', 1, FALSE),
                                                                     ('Car Maintenance', 1, FALSE);

-- Baby & Kids Subcategories (assuming category_id = 2)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Baby Care', 2, FALSE),
                                                                     ('Feeding and Nursing', 2, FALSE),
                                                                     ('Toys and Games', 2, FALSE),
                                                                     ('Kids Clothing', 2, FALSE);

-- Food & Grocery Subcategories (assuming category_id = 3)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Bakery and Bread', 3, FALSE),
                                                                     ('Beverages', 3, FALSE),
                                                                     ('Dairy and Eggs', 3, FALSE),
                                                                     ('Meat and Seafood', 3, FALSE),
                                                                     ('Fruits and Vegetables', 3, FALSE);


-- Health & Beauty Subcategories (assuming category_id = 4)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Health Supplements', 4, FALSE),
                                                                     ('Beauty and Cosmetics', 4, FALSE);


-- Home & Living Subcategories (assuming category_id = 5)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
    ('Kitchen and Dining', 5, FALSE),
    ('Home Decor', 5, FALSE),
    ('Cleaning Supplies', 5, FALSE),
    ('Furniture and Lighting', 5, FALSE);

-- Electronics & Gadgets Subcategories (assuming category_id = 6)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
    ('Mobile Phones and Accessories', 6, FALSE),
    ('Computers and Tablets', 6, FALSE),
    ('Audio and Video', 6, FALSE),
    ('Photography and Drones', 6, FALSE);

-- Fashion Subcategories (assuming category_id = 7)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
    ('Women Fashion', 7, FALSE),
    ('Men Fashion', 7, FALSE),
    ('Jewelry and Watches', 7, FALSE),
    ('Bags and Wallets', 7, FALSE);

-- Stationery & Books Subcategories (assuming category_id = 8)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
    ('Office Supplies', 8, FALSE),
    ('Art Supplies', 8, FALSE),
    ('Books', 8, FALSE);

-- Food & Spirits Subcategories (assuming category_id = 9)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
    ('Wines and Spirits', 9, FALSE),
    ('Non-Alcoholic Beverages', 9, FALSE),
    ('Snacks and Confectionery', 9, FALSE);


INSERT INTO branch (name, location) VALUES
                                        ('ValueMart Store', 'Maitama'),
                                        ('ValueMart Shop', 'Garki'),
                                        ('ValueMart Hub', 'Asokoro');

