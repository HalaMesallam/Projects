const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const fs = require('fs');

const { db } = require('../Database/db_connection');

exports.pharmacy_request = async_error_handler(async (req, res, next) => {
	const { pharmacy_id, pharmacy_name, pharmacy_address, pharmacy_image, owner_id } = req.body;

	var data = pharmacy_image.replace(/^data:image\/\w+;base64,/, "");

	var buf = new Buffer(data, 'base64');

	const sql = `SELECT * FROM pharmacies WHERE pharmacy_id = '${pharmacy_id}'`;

	db.query(sql, function (err, result, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		if (result.length > 0) {
			return next(new ErrorHandler("Pharmacy already exists with this id", 400));
		}

		fs.writeFile(`../client/public/Images/Pharmacy/${pharmacy_name}.png`, buf, function (err) {
			if (err) {
				console.log(err);
			}
		});

		const sql2 = `INSERT INTO pharmacies (pharmacy_id, pharmacy_name, pharmacy_address, pharmacy_image_path, pharmacy_owner_id, is_approved, pharmacy_total_reviews, pharmacy_ratings) VALUES ('${pharmacy_id}', '${pharmacy_name}', '${pharmacy_address}', '${pharmacy_name}.png', '${owner_id}', 0, 0, 0)`;

		db.query(sql2, function (err, result, fields) {
			if (err) {
				return next(new ErrorHandler("Database error", 500));
			}

			res.status(200).json({ success: true, message: "Pharmacy request sent" });
		})
	})
});

exports.medicine_request = async_error_handler(async (req, res, next) => {

	const { pharmacy_id, medicine_name, medicine_description, medicine_price, medicine_image, medicine_stock, medicine_expiry_date, owner_id } = req.body;

	var data = medicine_image.replace(/^data:image\/\w+;base64,/, "");

	var buf = new Buffer(data, 'base64');

	const sql = `SELECT * FROM pharmacies JOIN pharmacy_owners ON pharmacies.pharmacy_owner_id=pharmacy_owners.owner_id WHERE pharmacies.pharmacy_id=${pharmacy_id} AND pharmacy_owners.owner_id=${owner_id}`;

	db.query(sql, function (err, result, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		if (result.length == 0) {
			return next(new ErrorHandler("You are not the owner of this pharmacy", 400));
		}

		fs.writeFile(`../client/public/Images/Medicine/${medicine_name}-${pharmacy_id}.png`, buf, function (err) {
			if (err) {
				console.log(err);
			}
		});

		const sql2 = `INSERT INTO medicines (medicine_name, medicine_description, medicine_price, medicine_image, medicine_stock, medicine_expiry_date, pharmacy_id, is_approved, medicine_ratings, medicine_total_reviews) VALUES ('${medicine_name}', '${medicine_description}', '${medicine_price}', '${medicine_name}-${pharmacy_id}.png', '${medicine_stock}', '${medicine_expiry_date}', '${pharmacy_id}', 0, 0, 0)`;

		db.query(sql2, function (err, result, fields) {
			if (err) {
				return next(new ErrorHandler("Database error", 500));
			}

			res.status(200).json({ success: true, message: "Medicine request sent" });
		}
		)
	})
});

exports.owner_pharmacy_requests = async_error_handler(async (req, res, next) => {
	const id = req.params.id;

	console.log(id);

	const sql = `SELECT * FROM pharmacies WHERE pharmacy_owner_id=${id}`;

	db.query(sql, function (err, pharmacies, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		res.status(200).json({ success: true, pharmacies: pharmacies });
	}
	)
});

exports.owner_get_specific_pharmacy_order = async_error_handler(async (req, res, next) => {
	const id = req.params.id;

	const sql = `SELECT * FROM orders JOIN customers ON orders.customer_id=customers.customer_id JOIN pharmacies ON pharmacies.pharmacy_id = orders.pharmacy_id WHERE orders.pharmacy_id=${id}`;

	db.query(sql, function (err, orders, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		res.status(200).json({ success: true, orders: orders });
	}
	)
});

exports.owner_get_owner_details = async_error_handler(async_error_handler(async (req, res, next) => {
	const id = req.params.id;

	const sql = `SELECT * FROM order_contains JOIN orders ON order_contains.order_id = orders.order_id JOIN medicines ON medicines.medicine_id=order_contains.medicine_id JOIN customers ON customers.customer_id = orders.customer_id WHERE order_contains.order_id=${id}`;

	db.query(sql, function (err, order, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		res.status(200).json({ success: true, order: order });
	}
	)
}))
