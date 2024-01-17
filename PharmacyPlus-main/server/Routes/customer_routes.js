const router = require('express').Router();
const customer_controller = require("../Controllers/customer_controller.js");

router.route("/customer_orders/:id").get(customer_controller.customer_order);
router.route("/customer_order_details/:id").get(customer_controller.customer_order_details);

module.exports = router;
