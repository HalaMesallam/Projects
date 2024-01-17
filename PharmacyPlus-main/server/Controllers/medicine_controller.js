const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const { db } = require('../Database/db_connection');

exports.get_all_medicines_for_pharmacy = async_error_handler(async (req, res, next) => {

	const pharmacy_id = req.params.pharmacy_id;

	const sql = `SELECT * FROM medicines WHERE pharmacy_id = ${pharmacy_id}`;

	db.query(sql, function (err, medicines, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}
		res.status(200).json({ success: true, medicines: medicines });
	});
});

exports.get_medicine_by_id = async_error_handler(async (req, res, next) => {

	const medicine_id = req.params.medicine_id;
	const pharmacy_id = req.params.pharmacy_id;

	const sql = `SELECT * FROM medicines WHERE medicine_id=${medicine_id} AND pharmacy_id=${pharmacy_id}`;

	db.query(sql, function (err, medicine, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		res.status(200).json({ success: true, medicine: medicine });
	});
});

exports.update_medicine_review = async_error_handler(async(req, res, next) => {
	
	const medicine_id = req.params.medicine_id;
	const pharmacy_id = req.params.pharmacy_id;

	const { rating } = req.body;

	const sql = `SELECT medicine_ratings, medicine_total_reviews FROM medicines WHERE medicine_id=${medicine_id} AND pharmacy_id=${pharmacy_id}`;

	db.query(sql, function (err, medicine, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		const medicine_ratings = medicine[0].medicine_ratings;
		const medicine_total_reviews = medicine[0].medicine_total_reviews;

		const new_medicine_total_reviews = medicine_total_reviews + 1;
		const new_medicine_ratings = ((parseFloat(medicine_ratings) * parseFloat(medicine_total_reviews)) + parseFloat(rating))/parseFloat(new_medicine_total_reviews);

		const sql2 = `UPDATE medicines SET medicine_ratings=${new_medicine_ratings}, medicine_total_reviews=${new_medicine_total_reviews} WHERE medicine_id=${medicine_id} AND pharmacy_id=${pharmacy_id}`;

		db.query(sql2, function (err, result, fields) {
			if (err) {
				return next(new ErrorHandler("Database error", 500));
			}

			res.status(200).json({ success: true, message: "Review updated successfully" });
		});
})});
