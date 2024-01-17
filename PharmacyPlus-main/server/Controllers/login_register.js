const async_error_handler = require('../Middleware/async_catch');
const ErrorHandler = require('../Utils/ErrorHandler');

const { db } = require('../Database/db_connection');

exports.admin_login = async_error_handler(async (req, res, next) => {

	const { email, password } = req.body;

	if (!email || !password) {
		return next(new ErrorHandler('Please provide email and password', 400));
	}

	db.query("SELECT * FROM admins WHERE admin_email=? AND admin_password=?", [email, password], function (err, admin, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		if (admin.length === 0) {
			return next(new ErrorHandler('Invalid email or password', 401));
		}

		res.status(200).json({ success: true, admin: admin, is_admin: true, is_customer: false, is_owner: false });
	});
})

exports.pharmacy_owner_login = async_error_handler(async (req, res, next) => {
	
	const { email, password } = req.body;

	if (!email || !password) {
		return next(new ErrorHandler('Please provide email and password', 400));
	}

	db.query("SELECT * FROM pharmacy_owners WHERE email=? AND Password=?", [email, password], function (err, owner, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		if (owner.length === 0) {
			return next(new ErrorHandler('Invalid email or password', 401));
		}

		res.status(200).json({ success: true, owner: owner, is_owner: true });
	});
})

exports.pharmacy_owner_register = async_error_handler(async (req, res, next) => {
	
	const { name, email, password } = req.body;

	if (!name || !email || !password) {
		return next(new ErrorHandler('Please provide all the fields', 400));
	}

	db.query("SELECT * FROM pharmacy_owners WHERE email=?", [email], function (err, owner, fields) {
		if (err) {
			return next 
		}

		if (owner.length > 0) {
			return next(new ErrorHandler('Email already exists', 400));
		}

		db.query("INSERT INTO pharmacy_owners (owner_name, email, Password) VALUES (?, ?, ?)", [name, email, password], function (err, result, fields) {
			if (err) {
				return next(new ErrorHandler("Database error", 500));
			}

			db.query("SELECT * FROM pharmacy_owners WHERE email=? AND Password=?", [email, password], function (err, owner, fields) {
				if (err) {
					return next(new ErrorHandler("Database error", 500));
				}

				if (owner.length === 0) {
					return next(new ErrorHandler('Invalid email or password', 401));
				}
				
				res.status(200).json({ success: true, owner: owner, is_owner: true });
			});
		});
	})
});

exports.customer_login = async_error_handler(async (req, res, next) => {
	
	const { email, password } = req.body;

	if (!email || !password) {
		return next(new ErrorHandler('Please provide email and password', 400));
	}

	db.query("SELECT * FROM customers WHERE customer_email=? AND account_password=?", [email, password], function (err, customer, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		if (customer.length === 0) {
			return next(new ErrorHandler('Invalid email or password', 401));
		}

		res.status(200).json({ success: true, customer: customer, is_customer: true });
	});
});

exports.customer_register = async_error_handler(async (req, res, next) => {
	
	const { email, password, name, phone, address } = req.body;

	if (!email || !password || !name || !phone) {
		return next(new ErrorHandler('Please provide all required fields', 400));
	}

	db.query("SELECT * FROM customers WHERE customer_email=?", [email], function (err, customer, fields) {
		if (err) {
			return next(new ErrorHandler("Database error", 500));
		}

		if (customer.length > 0) {
			return next(new ErrorHandler('Email already exists', 400));
		}

		db.query("INSERT INTO customers (customer_email, customer_name, customer_address, customer_phone, account_password) VALUES (?, ?, ?, ?, ?)", [email, name, address, phone, password], function (err, customer, fields) {
			if (err) {
				return next(new ErrorHandler("Database error", 500));
			}

			db.query("SELECT * FROM customers WHERE customer_email=? AND account_password=?", [email, password], function (err, customer, fields) {
				if (err) {
					return next(new ErrorHandler("Database error", 500));
				}
				
				res.status(200).json({ success: true, customer: customer, is_customer: true });
			});
		});
	});
})

