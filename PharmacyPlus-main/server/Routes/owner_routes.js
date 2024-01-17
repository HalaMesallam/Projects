const router = require('express').Router();
const owner_controller = require("../Controllers/owner_controller");

router.route("/request_pharmacy").post(owner_controller.pharmacy_request);
router.route("/request_medicine").post(owner_controller.medicine_request);
router.route("/owner_pharmacy_requests/:id").get(owner_controller.owner_pharmacy_requests);
router.route("/owner_get_specific_pharmacy_order/:id").get(owner_controller.owner_get_specific_pharmacy_order);
router.route("/owner_order_details/:id").get(owner_controller.owner_get_owner_details);

module.exports = router;
