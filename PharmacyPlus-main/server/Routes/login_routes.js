const router = require('express').Router();
const login_register = require("../Controllers/login_register");

router.route("/login_admin").post(login_register.admin_login);
router.route("/login_pharmacy_owner").post(login_register.pharmacy_owner_login);
router.route("/register_pharmacy_owner").post(login_register.pharmacy_owner_register);
router.route("/login_customer").post(login_register.customer_login);
router.route("/register_customer").post(login_register.customer_register);

module.exports = router;

