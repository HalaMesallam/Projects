const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const { db } = require('../Database/db_connection');

exports.customer_order = async_error_handler(async (req, res, next) => {

	const customer_id = req.params.id;

	const sql = `SELECT * FROM orders JOIN customers ON orders.customer_id=customers.customer_id JOIN pharmacies ON pharmacies.pharmacy_id = orders.pharmacy_id WHERE orders.customer_id = ${customer_id}`;

	db.query(sql, function (err, orders, fields) {
		if (err) {
			return next(new ErrorHandler("Database Error", 500));
		}
		res.status(200).json({ success: true, orders: orders });
	}
)});

exports.customer_order_details = async_error_handler(async (req, res, next) => {
	
	const order_id = req.params.id;

	const sql = `SELECT * FROM order_contains JOIN medicines ON order_contains.medicine_id=medicines.medicine_id WHERE order_contains.order_id=${order_id}`;

	db.query(sql, function (err, order, fields) {
		if (err) {
			return next(new ErrorHandler("Database Error", 500));
		}
		res.status(200).json({ success: true, order: order });
	}
)});
