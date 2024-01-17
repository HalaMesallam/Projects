const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const { db } = require('../Database/db_connection');

exports.create_order = async_error_handler(async (req, res, next) => {
	const { cartItems, shippingInfo, tax, total, user } = req.body;

	const customer_id = user.customer[0].customer_id;
	const pharmacy_id = cartItems[0].pharmacy_id;

	const address = shippingInfo.address;

	db.query("INSERT INTO orders (customer_id, pharmacy_id, order_address, tax_price, total_price) VALUES (?, ?, ?, ?, ?)", [customer_id, pharmacy_id, address, tax, total], function (err, order, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		const order_id = order.insertId;

		for (let i = 0; i < cartItems.length; i++) {
			db.query("INSERT INTO order_contains (order_id, medicine_id, quantity) VALUES (?, ?, ?)", [order_id, cartItems[i].medicine_id, cartItems[i].medicine_quantity], function (err, orderItem, fields) {
				if (err) {
					return next(new ErrorHandler("Database error", 500));
				}
			});

			const medicine_stock = cartItems[i].medicine_stock;
			const new_medicine_stock = medicine_stock - cartItems[i].medicine_quantity;

			db.query("UPDATE medicines SET medicine_stock=(?) WHERE medicine_id=(?) AND pharmacy_id=(?)", [new_medicine_stock, cartItems[i].medicine_id, cartItems[i].pharmacy_id], function (err, orderItem, fields){
				if (err){
					return next(new ErrorHandler("Database error", 500));
				}
			});
		}

		res.status(200).json({ success: true, order: order });
	});
});
