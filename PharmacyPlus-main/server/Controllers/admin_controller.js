const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const { db } = require('../Database/db_connection');

exports.change_pharmacy_approval = async_error_handler(async (req, res, next) => {
	
	let { pharmacy_id, is_approved } = req.body;

	console.log(pharmacy_id, is_approved);

	if (!pharmacy_id || !is_approved) {
		return next(new ErrorHandler('Please provide all the fields', 400));
	}

	if (is_approved === 'approved') {
		is_approved = 1;
	} else {
		is_approved = 0;
	}

	db.query("UPDATE pharmacies SET is_approved=? WHERE pharmacy_id=?", [is_approved, pharmacy_id], function (err, result, fields) {
		if (err) {
			return next(new ErrorHandler('Database error', 500));
		}

		if (result.affectedRows === 0) {
			return next(new ErrorHandler('Pharmacy not found', 404));
		}

		res.status(200).json({ success: true });
	});
});

exports.admin_get_all_medicines = async_error_handler(async (req, res, next) => {
	
	db.query("SELECT * FROM medicines", function (err, result, fields) {
		if (err) {
			return next(new ErrorHandler('Database error', 500));
		}

		res.status(200).json({ medicines: result });
	});
})

exports.change_medicine_approval = async_error_handler(async (req, res, next) => {
	
	let { medicine_id, is_approved } = req.body;

	if (!medicine_id || !is_approved) {
		return next(new ErrorHandler('Please provide all the fields', 400));
	}

	if (is_approved === 'approved') {
		is_approved = 1;
	} else {
		is_approved = 0;
	}

	db.query("UPDATE medicines SET is_approved=? WHERE medicine_id=?", [is_approved, medicine_id], function (err, result, fields) {
		if (err) {
			return next(new ErrorHandler('Database error', 500));
		}

		if (result.affectedRows === 0) {
			return next(new ErrorHandler('Medicine not found', 404));
		}

		res.status(200).json({ success: true });
	});
})
