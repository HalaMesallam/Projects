const app = require('./app');
const port = process.env.PORT || 4000;
const db = require('./Database/db_connection');

require('dotenv').config({ path: ".env" });

// Handling uncaught exceptions. Handle errors when you use undefined variables.
process.on('uncaughtException', (err) => {
	console.log("Uncaught exception: " + err.message);

	console.log("Shutting down the server due to an error: " + err.message);

	process.exit(1);

});

// Connect to the database.
db.connect_db();

const server = app.listen(port, (err) => {
	if (err) {
		console.log("There is an error connecting to the server", err.message);
	}
	else {
		console.log("Server is running on port", port);
	}
});

// UnHandle Promise Rejection: Only one thing happens at promise time.
process.on("unhandledRejection", (err) => {
	console.log("There is an unhandled Error: " + err.stack);

	console.log("Shutting down the server due to an error: " + err.message);

	server.close(() => {
		process.exit(1);
	});
});
