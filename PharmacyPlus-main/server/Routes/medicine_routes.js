const router = require('express').Router();
const medicine = require('../Controllers/medicine_controller');

router.route("/all_medicines/:pharmacy_id").get(medicine.get_all_medicines_for_pharmacy);
router.route("/medicine/:pharmacy_id/:medicine_id").get(medicine.get_medicine_by_id);

router.route("/medicine/update_review/:pharmacy_id/:medicine_id").put(medicine.update_medicine_review)

module.exports = router;
