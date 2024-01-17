const mysql = require('mysql2');
const dotenv = require('dotenv');

dotenv.config({ path: ".env" });

// Create a connection to the database

const db = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: process.env.password,
	database: process.env.db_name
});

// Connect to the database
exports.connect_db = () => {
	db.connect((err) => {
		if (err) {
			console.log("There is an error connecting to the database: " + err.message);
		}
		else {
			console.log("Database is connected Successfully.");
		}
	});
};

// Query the database
exports.db = db;
