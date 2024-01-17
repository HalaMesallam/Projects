const ErrorHandler = require("../Utils/ErrorHandler");

module.exports = (err, req, res, next) => {
	err.statusCode = err.statusCode || 500;
	err.message = err.message || "Internal Server Error";

	// Duplicate key error.
	if (err.code === 11000) {
		const message = "Duplicate Primary Keys.";

		err = new ErrorHandler(message, 11000);
	}

	console.log(err);

	res.status(err.statusCode).json({success: false, message: err.message});
}
