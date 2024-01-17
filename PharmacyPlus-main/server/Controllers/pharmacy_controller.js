const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const { db } = require('../Database/db_connection');

exports.get_all_pharmacies = async_error_handler(async (req, res, next) => {

	db.query("SELECT * FROM pharmacies", function (err, pharmacies, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		res.status(200).json({ success: true, pharmacies: pharmacies });
	});
});

exports.get_pharmacy_details = async_error_handler(async (req, res, next) => {
	
	const pharmacy_id = req.params.pharmacy_id;

	db.query("SELECT * FROM pharmacies JOIN pharmacy_owners ON owner_id=pharmacy_owner_id WHERE pharmacy_id = ?", [pharmacy_id], function (err, pharmacy, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		res.status(200).json({ success: true, pharmacy: pharmacy });
	});
});

exports.add_pharmacy_review = async_error_handler(async (req, res, next) => {
	
	const pharmacy_id = req.params.pharmacy_id;
	const rating = req.body.rating;

	const sql = `SELECT pharmacy_ratings, pharmacy_total_reviews FROM pharmacies WHERE pharmacy_id=${pharmacy_id}`;

	db.query(sql, function (err, pharmacy, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		const pharmacy_ratings = pharmacy[0].pharmacy_ratings;
		const pharmacy_total_reviews = pharmacy[0].pharmacy_total_reviews;

		const new_pharmacy_total_reviews = pharmacy_total_reviews + 1;
		const new_pharmacy_ratings = ((parseFloat(pharmacy_ratings) * parseFloat(pharmacy_total_reviews)) + parseFloat(rating))/parseFloat(new_pharmacy_total_reviews);

		const sql2 = `UPDATE pharmacies SET pharmacy_ratings=${new_pharmacy_ratings}, pharmacy_total_reviews=${new_pharmacy_total_reviews} WHERE pharmacy_id=${pharmacy_id}`;

		db.query(sql2, function (err, result, fields) {
			if (err) {
				return next(new ErrorHandler("Database error", 500));
			}

			res.status(200).json({ success: true, message: "Review added successfully" });
		})
	});
});
