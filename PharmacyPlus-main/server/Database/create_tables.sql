DROP SCHEMA IF EXISTS `pharmacy_plus_db`;

CREATE SCHEMA `pharmacy_plus_db`;

USE `pharmacy_plus_db`;

CREATE TABLE pharmacy_owners (
	owner_id int NOT NULL AUTO_INCREMENT UNIQUE,  
    owner_name varchar(255) NOT NULL,  
    email varchar(255) UNIQUE NOT NULL,  
    Password varchar(255) NOT NULL,
    PRIMARY KEY (owner_id, email) 
);

CREATE TABLE pharmacies (  
	pharmacy_id int NOT NULL,  
    pharmacy_name varchar(255) NOT NULL,  
    pharmacy_address varchar(255) NOT NULL,  
    pharmacy_image_path varchar(255) UNIQUE,     
    pharmacy_ratings float DEFAULT 0,
    pharmacy_total_reviews float DEFAULT 0,  
    is_approved BOOLEAN NOT NULL,
    pharmacy_owner_id int,
    PRIMARY KEY (pharmacy_id),
    FOREIGN KEY (pharmacy_owner_id) REFERENCES pharmacy_owners (owner_id)
);

CREATE TABLE medicines (  
	medicine_id int NOT NULL AUTO_INCREMENT UNIQUE,  
    pharmacy_id int NOT NULL,  
    medicine_name varchar(255) NOT NULL,  
    medicine_description varchar(1000) DEFAULT "",  
    medicine_stock int NOT NULL,  
    medicine_price float NOT NULL,  
    medicine_expiry_date DATETIME NOT NULL,  
    medicine_image varchar(255) UNIQUE,     
    medicine_ratings float DEFAULT 0,     
    medicine_total_reviews int DEFAULT 0,  
    is_approved BOOLEAN NOT NULL,  
    PRIMARY KEY (medicine_id, pharmacy_id),  
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (pharmacy_id) 
);

CREATE TABLE customers (
	customer_id int NOT NULL AUTO_INCREMENT UNIQUE,
    customer_email varchar(255) UNIQUE NOT NULL,  
    customer_name varchar(255) NOT NULL,  
    customer_address varchar(255) NOT NULL,  
    customer_phone varchar(10) NOT NULL,  
    account_password varchar(255) NOT NULL,  
    PRIMARY KEY (customer_id, customer_email) 
);

CREATE TABLE orders (
	order_id int NOT NULL UNIQUE AUTO_INCREMENT,  
    pharmacy_id int NOT NULL,  
    total_price float NOT NULL,
    tax_price float NOT NULL,
    customer_id int NOT NULL,  
    order_address varchar(255) NOT NULL,  
    PRIMARY KEY (order_id),  
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),     
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (pharmacy_id) 
);

CREATE TABLE admins (  
	admin_id int NOT NULL UNIQUE,     
    admin_name varchar(255) NOT NULL,  
    admin_email varchar(255) UNIQUE NOT NULL, 
    admin_password varchar(255) NOT NULL,  
    PRIMARY KEY (admin_id, admin_email) 
);

CREATE TABLE order_contains (  
	order_id int NOT NULL,
    medicine_id int NOT NULL,  
    quantity int NOT NULL,  
    PRIMARY KEY (order_id, medicine_id),  
    FOREIGN KEY (order_id) REFERENCES orders (order_id),  
    FOREIGN KEY (medicine_id) REFERENCES Medicines (medicine_id)
);

INSERT INTO `pharmacy_plus_db`.`admins` 
	(`admin_id`, `admin_name`, `admin_email`, `admin_password`) VALUES 
	('1', 'Adarsh', 'adarsh.dudhat@ucalgary.ca', 'Password');

INSERT INTO `pharmacy_plus_db`.`admins` 
	(`admin_id`, `admin_name`, `admin_email`, `admin_password`) VALUES 
	('2', 'Hala', 'hala.mesallam@ucalgary.ca', 'Password');

INSERT INTO `pharmacy_plus_db`.`admins` 
	(`admin_id`, `admin_name`, `admin_email`, `admin_password`) VALUES 
    ('3', 'Rayan', 'rayan.khalil@ucalgary.ca', 'Password');
