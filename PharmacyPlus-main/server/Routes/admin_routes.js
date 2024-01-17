const router = require('express').Router();
const admin_controller = require("../Controllers/admin_controller");

router.route("/update_pharmacy_approval").put(admin_controller.change_pharmacy_approval);
router.route("/update_medicine_approval").put(admin_controller.change_medicine_approval);
router.route("/admin_get_all_medicines").get(admin_controller.admin_get_all_medicines);

module.exports = router;
