const express = require('express');
const errorMiddleware = require("./Middleware/errors");
const body_parser = require('body-parser');
const app = express();
const cors = require('cors');

app.use(cors());

app.use(body_parser.urlencoded({
	extended: true
}));
app.use(body_parser.json({ limit: '50mb' }));

const pharmacy_routes = require("./Routes/pharmacy_routes");
const medicine_routes = require("./Routes/medicine_routes");
const login_routes = require("./Routes/login_routes");
const admin_routes = require("./Routes/admin_routes");
const owner_routes = require("./Routes/owner_routes");
const customer_routes = require("./Routes/customer_routes");
const order_routes = require("./Routes/order_routes");

app.use('/api/', pharmacy_routes);
app.use('/api/', medicine_routes);
app.use('/api/', login_routes);
app.use('/api/', admin_routes);
app.use('/api/', owner_routes);
app.use('/api/', customer_routes);
app.use('/api/', order_routes);

app.use(errorMiddleware);

module.exports = app;
