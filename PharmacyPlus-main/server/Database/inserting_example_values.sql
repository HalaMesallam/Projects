USE pharmacy_plus_db;

-- Add Owners

INSERT INTO `pharmacy_plus_db`.`pharmacy_owners` (`owner_id`, `owner_name`, `email`, `Password`) VALUES 
('1', 'Alex', 'owner1@ucalgary.ca', 'OwnerPassword');

INSERT INTO `pharmacy_plus_db`.`pharmacy_owners` (`owner_id`, `owner_name`, `email`, `Password`) VALUES 
('2', 'James', 'owner2@ucalgary.ca', 'OwnerPassword');

INSERT INTO `pharmacy_plus_db`.`pharmacy_owners` (`owner_id`, `owner_name`, `email`, `Password`) VALUES 
('3', 'Olivia', 'owner3@ucalgary.ca', 'OwnerPassword');


-- Add Pharmacies

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('1', 'London Drugs', '5255 Richmond Rd SW Suite 300, Calgary, AB T3E 7C4', 'London Drugs.png', '4', '1', '1', '1');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('2', 'Dominion', '665 8 Street SW Calgary, AB T2P 3K7', 'Dominion.png', '3', '1', '1', '2');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('3', 'Safeway Pharmacy', '1200 37 Street SW, Calgary, AB T3C 1S2', 'Safeway Pharmacy.png', '0', '0', '0', '1');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('4', 'Midtown Remedys Pharmacy', '1110 11 Ave SW, Calgary, AB T2R 0G4', 'Midtown Remedys Pharmacy.png', '5', '3', '1', '1');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('5', 'Co-op Pharmacy', '1130 11 Ave SW, Calgary, AB T2R 0G4', 'Co-op Pharmacy.png', '3', '1', '1', '1');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('6', 'Beacon Pharmacy', '1213 4 St SW, Calgary, AB T2R 0X7', 'Beacon Pharmacy.png', '5', '3', '1', '1');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('7', 'Shoppers Drug Mart', '317 7 Ave SW Unit T 100, Calgary, AB T2P 2Y9', 'Shoppers Drug Mart.png', '0', '0', '1', '1');

INSERT INTO `pharmacy_plus_db`.`pharmacies` (`pharmacy_id`, `pharmacy_name`, `pharmacy_address`, `pharmacy_image_path`, `pharmacy_ratings`, `pharmacy_total_reviews`, `is_approved`, `pharmacy_owner_id`) VALUES 
('8', 'Drug Line', '1010 8 Ave SW, Calgary, AB T2P 1J2', 'Drug Line.png', '5', '4', '1', '2');

-- Add Medicines

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('1', 'Tylenol', 'When used as directed, it safely reduces fever and relieves many kinds of mild to moderate pain -- from backaches, headaches, and sprains to arthritis and menstrual cramps.', '10', '5', '2024-11-11', 'Tylenol-1.png', '5', '3', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('2', 'iBuprofen', 'It is best used for fever, aches and pains, but will not be very helpful if the pain is due to inflammation. Ibuprofen is more helpful for these symptoms when inflammation is the cause. Inflammation examples include menstrual cramps and arthritis.', '50', '10', '2023-01-24', 'iBuprofen-2.png', '4', '10', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('1', 'Aspirin', 'Aspirin is used to reduce fever and to relieve mild to moderate pain from headaches, menstrual periods, arthritis, toothaches, and muscle aches.', '12', '2', '2025-02-22', 'Aspirin-1.png', '2.5', '2', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('1', 'Albuterol', 'Albuterol is used to treat or prevent bronchospasm in patients with asthma, bronchitis, emphysema, and other lung diseases. It is also used to prevent bronchospasm caused by exercise. Albuterol belongs to the family of medicines known as adrenergic bronchodilators.', '100', '3.69', '2023-12-02', 'Albuterol-1.png', '5', '2', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('1', 'Gabapentin', 'Gabapentin is used to treat epilepsy. It is also taken for nerve pain, which can be caused by different conditions, including diabetes and shingles. Nerve pain can also happen after an injury. In epilepsy, it is thought that gabapentin stops seizures by reducing the abnormal electrical activity in the brain.', '2', '5.86', '2024-09-05', 'Gabapentin-1.png', '4', '1', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('1', 'Metformin', 'Metformin is used to treat high blood sugar levels that are caused by a type of diabetes mellitus or sugar diabetes called type 2 diabetes. With this type of diabetes, insulin produced by the pancreas is not able to get sugar into the cells of the body where it can work properly.', '30', '4.12', '2026-01-01', 'Metformin-1.png', '0', '0', '0');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('2', 'Tylenol', 'Acetaminophen is most commonly used to treat minor aches and pains, including headache, backache, minor pain of arthritis, toothache, muscular aches, premenstrual and menstrual cramps. It is also commonly used to temporarily reduce fever.', '10', '5', '2024-01-01', 'Tylenol-2.png', '0', '0', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('2', 'Losartan', 'Losartan is used alone or together with other medicines to treat high blood pressure (hypertension). High blood pressure adds to the workload of the heart and arteries. If it continues for a long time, the heart and arteries may not function properly.', '15', '15.67', '2028-01-01', 'Losartan-2.png', '2.5', '6', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('3', 'Omeprazole', 'Omeprazole reduces the amount of acid your stomach makes. It is widely used to treat indigestion and heartburn, and acid reflux. It is also taken to prevent and treat stomach ulcers. Omeprazole is a type of medicine called a proton pump inhibitor (PPI).', '30', '17.67', '2026-12-31', 'Omeprazole-3.png', '4', '2', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('3', 'Ventolin', 'VENTOLIN HFA is a prescription inhaled medicine used to treat or prevent bronchospasm in people aged 4 years and older with reversible obstructive airway disease. VENTOLIN HFA is also used to prevent exercise-induced bronchospasm (EIB) in patients aged 4 years and older.', '50', '176.67', '2040-12-31', 'Ventolin-3.png', '5', '200', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('3', 'Azithromycin', 'Azithromycin is used to treat certain bacterial infections, such as bronchitis; pneumonia; sexually transmitted diseases (STD); and infections of the ears, lungs, sinuses, skin, throat, and reproductive organs.', '50', '1.67', '2025-03-01', 'Azithromycin-3.png', '4', '8', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('4', 'Tylenol', 'Acetaminophen helps to reduce fever and/or mild to moderate pain (such as headache, backache, aches/pains due to muscle strain, cold, or flu). The antihistamine in this product may cause drowsiness, so it can also be used as a nighttime sleep aid.', '14', '3.19', '2024-01-01', 'Tylenol-4.png', '0', '0', '0');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('5', 'Advil', 'It is one of a group of painkillers called non-steroidal anti-inflammatory drugs (NSAIDs) and can be used to: ease mild to moderate pain – such as toothache, migraine and period pain. control a fever (high temperature) – for example, when someone has the flu (influenza)', '149', '2.99', '2025-04-15', 'Advil-5.png', '3.5', '25', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('5', 'Amoxicillin', 'It is used to treat bacterial infections, such as chest infections (including pneumonia) and dental abscesses. It can also be used together with other antibiotics and medicines to treat stomach ulcers. It is often prescribed for children, to treat ear infections and chest infections.', '12', '7.99', '2023-01-31', 'Amoxicillin-5.png', '4.5', '2', '0');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('6', 'Basaglar', 'BASAGLAR is a long-acting insulin that is only available with a prescription. It is used to control high blood sugar in: adults with type 1 or type 2 diabetes. children with type 1 diabetes.', '50', '140.22', '2029-04-02', 'Basaglar-6.png', '4', '67', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('6', 'Metoprolol Succinate', 'This medication is a beta-blocker used to treat chest pain (angina), heart failure, and high blood pressure. Lowering high blood pressure helps prevent strokes, heart attacks, and kidney problems.', '4', '14.22', '2025-04-01', 'Metoprolol Succinate-6.png', '5', '12', '1');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('6', 'Ondansetron', 'Ondansetron is used to prevent nausea and vomiting that is caused by cancer medicines (chemotherapy) or radiation therapy. It is also used to prevent nausea and vomiting that may occur after surgery. Ondansetron works in the stomach to block the signals to the brain that cause nausea and vomiting.', '10', '5.68', '2024-07-16', 'Ondansetron-6.png', '0', '0', '0');

INSERT INTO `pharmacy_plus_db`.`medicines` (`pharmacy_id`, `medicine_name`, `medicine_description`, `medicine_stock`, `medicine_price`, `medicine_expiry_date`, `medicine_image`, `medicine_ratings`, `medicine_total_reviews`, `is_approved`) VALUES 
('7', 'Proair', 'ProAir (albuterol sulfate) is a bronchodilator that relaxes muscles in the airways and increases air flow to the lungs used to prevent and treat wheezing and shortness of breath caused by breathing problems such as asthma and chronic obstructive pulmonary disease.', '100', '1.23', '2026-01-12', 'Proair-7.png', '1', '2', '1');

-- Add Customers

INSERT INTO `pharmacy_plus_db`.`customers` (`customer_email`, `customer_name`, `customer_address`, `customer_phone`, `account_password`) VALUES 
('customer1@gmail.com', 'Customer1', '505 100St SW Calgary, AB T2N 1N4', '1234567890', 'CustomerPassword');

INSERT INTO `pharmacy_plus_db`.`customers` (`customer_email`, `customer_name`, `customer_address`, `customer_phone`, `account_password`) VALUES 
('customer2@gmail.com', 'Customer2', '912 6 Ave SW, Calgary, AB', '0987654321', 'CustomerPassword');

INSERT INTO `pharmacy_plus_db`.`customers` (`customer_email`, `customer_name`, `customer_address`, `customer_phone`, `account_password`) VALUES 
('customer3@gmail.com', 'Customer3', '609 8 St SW, Calgary, AB', '9999999999', 'CustomerPassword');


-- Add Orders

INSERT INTO `pharmacy_plus_db`.`orders` (`pharmacy_id`, `total_price`, `customer_id`, `order_address`, `tax_price`) VALUES 
('1', '15.75', '1', '609 8 St SW, Calgary, AB', '0.75');

INSERT INTO `pharmacy_plus_db`.`orders` (`pharmacy_id`, `total_price`, `customer_id`, `order_address`, `tax_price`) VALUES 
('2', '26.25', '1', '2500 University Dr NW, Calgary, AB', '1.25');

INSERT INTO `pharmacy_plus_db`.`orders` (`pharmacy_id`, `total_price`, `customer_id`, `order_address`, `tax_price`) VALUES 
('1', '2.1', '2', '101 9 Ave SW, Calgary, AB', '0.1');


-- Add Order contains items

INSERT INTO `pharmacy_plus_db`.`order_contains` (`order_id`, `medicine_id`, `quantity`) VALUES 
('1', '1', '3');

INSERT INTO `pharmacy_plus_db`.`order_contains` (`order_id`, `medicine_id`, `quantity`) VALUES 
('2', '7', '1');

INSERT INTO `pharmacy_plus_db`.`order_contains` (`order_id`, `medicine_id`, `quantity`) VALUES 
('2', '2', '2');

INSERT INTO `pharmacy_plus_db`.`order_contains` (`order_id`, `medicine_id`, `quantity`) VALUES 
('3', '3', '1');
