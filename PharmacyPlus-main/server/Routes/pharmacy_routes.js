const router = require('express').Router();
const pharmacy = require('../Controllers/pharmacy_controller');

router.route("/all_pharmacies").get(pharmacy.get_all_pharmacies);
router.route("/pharmacy_details/:pharmacy_id").get(pharmacy.get_pharmacy_details);

router.route("/add_pharmacy_review/:pharmacy_id").put(pharmacy.add_pharmacy_review);

module.exports = router;
