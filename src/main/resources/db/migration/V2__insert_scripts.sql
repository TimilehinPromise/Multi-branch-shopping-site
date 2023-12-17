INSERT INTO business_category (name, is_deleted) VALUES
                                                                 ('Electronics', FALSE),
                                                                 ('Fashion', FALSE),
                                                                 ('Home and Garden', FALSE),
                                                                 ('Sports and Outdoors', FALSE),
                                                                 ('Beauty and Personal Care', FALSE),
                                                                 ('Toys and Games', FALSE),
                                                                 ('Books and Stationery', FALSE),
                                                                 ('Food and Grocery', FALSE);


-- For 'Electronics' Category
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Mobile Phones', 1, FALSE),
                                                                     ('Laptops and Computers', 1, FALSE),
                                                                     ('Cameras and Photography', 1, FALSE),
                                                                     ('Audio and Headphones', 1, FALSE),
                                                                     ('Wearable Technology', 1, FALSE);

INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Women Clothing', 2, FALSE),
                                                                      ('Men Clothing', 2, FALSE);

-- For 'Home and Garden' Category (category_id = 3)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Furniture', 3, FALSE),
                                                                     ('Home Decor', 3, FALSE),
                                                                     ('Kitchenware', 3, FALSE),
                                                                     ('Gardening Tools', 3, FALSE),
                                                                     ('Home Appliances', 3, FALSE);

-- For 'Sports and Outdoors' Category (category_id = 4)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Fitness Equipment', 4, FALSE),
                                                                     ('Camping and Hiking', 4, FALSE),
                                                                     ('Cycling', 4, FALSE),
                                                                     ('Water Sports', 4, FALSE),
                                                                     ('Team Sports Gear', 4, FALSE);

-- For 'Beauty and Personal Care' Category (category_id = 5)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                     ('Skincare Products', 5, FALSE),
                                                                     ('Haircare Products', 5, FALSE),
                                                                     ('Makeup and Cosmetics', 5, FALSE),
                                                                     ('Fragrances', 5, FALSE),
                                                                     ('Mens Grooming', 5, FALSE);

-- For 'Toys and Games' Category (category_id = 6)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                    ('Educational Toys', 6, FALSE),
                                                                    ('Board Games and Puzzles', 6, FALSE),
                                                                    ('Dolls and Action Figures', 6, FALSE),
                                                                    ('Outdoor Play', 6, FALSE),
                                                                    ('Video Games', 6, FALSE);

-- For 'Books and Stationery' Category (category_id = 7)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                    ('Fiction Books', 7, FALSE),
                                                                    ('Non-Fiction Books', 7, FALSE),
                                                                    ('Children’s Books', 7, FALSE),
                                                                    ('Office Supplies', 7, FALSE),
                                                                    ('Art and Craft Supplies', 7, FALSE);

-- For 'Food and Grocery' Category (category_id = 8)
INSERT INTO business_subcategory (name, category_id, is_deleted) VALUES
                                                                    ('Fresh Produce', 8, FALSE),
                                                                    ('Dairy and Eggs', 8, FALSE),
                                                                    ('Snacks and Beverages', 8, FALSE),
                                                                    ('Baking and Cooking Ingredients', 8, FALSE),
                                                                    ('Health and Organic Foods', 8, FALSE);

INSERT INTO branch (name, location) VALUES
                                        ('ValueMart Store', 'Maitama'),
                                        ('ValueMart Shop', 'Garki'),
                                        ('ValueMart Hub', 'Asokoro');

