const router = require('express').Router();
const order_controller = require("../Controllers/order_controller.js");

router.route("/orders/newOrder").post(order_controller.create_order);

module.exports = router;
