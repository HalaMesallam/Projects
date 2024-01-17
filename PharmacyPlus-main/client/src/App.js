import './App.css';
import { Route, Routes } from 'react-router-dom';

import Home from './Components/Home/Home';
import PharmacyMedicine from './Components/PharmacyMedicines/PharmacyMedicine';
import SpecificMedicine from './Components/SpecificMedicine/SpecificMedicine.js';
import AdminLogin from './Components/Login/AdminLogin';
import AdminAccount from './Components/Accounts/AdminAccount';
import AdminPharmacies from './Components/Admin/AdminPharmacies';
import AdminPharmacyDetails from './Components/Admin/AdminPharmacyDetails';
import AdminMedicines from './Components/Admin/AdminMedicines';
import AdminMedicineDetail from './Components/Admin/AdminMedicineDetail';
import OwnerLogin from './Components/Login/OwnerLogin';
import OwnerAccount from './Components/Accounts/OwnerAccount';
import PharmacyRequest from './Components/OwnerRequests/PharmacyRequest';
import MedicineRequest from './Components/OwnerRequests/MedicineRequest';
import CustomerLogin from './Components/Login/CustomerLogin.js';
import CustomerAccount from './Components/Accounts/CustomerAccount.js';
import CustomerOrders from './Components/Orders/CustomerOrders.js';
import OrderDetails from './Components/Orders/OrderDetails.js';
import OwnerPharmacyOrder from './Components/Orders/OwnerPharmacyOrders.js';
import OwnerSpecificPharmacyOrders from './Components/Orders/OwnerSpecificPharmacyOrders.js';
import OwnerOrderDetails from './Components/Orders/OwnerOrderDetails.js';
import NotFound from './Components/NotFound/NotFound.js';
import Cart from './Components/Cart/Cart.js';
import ShippingInfo from './Components/Shipping/ShippingInfo.js';
import OrderReview from './Components/ReviewPayment/OrderReview.js';
import Payment from './Components/ReviewPayment/Payment.js';
import Success from './Components/ReviewPayment/Success.js';

function App() {
	return (
		<Routes>
			<Route path="/" element={<Home />} />
			<Route path="/pharmacy/medicines/:id" element={<PharmacyMedicine />} />
			<Route path='/pharmacy/medicines/:pharmacy_id/:medicine_id' element={<SpecificMedicine />} />
			<Route path="/adminLogin" element={<AdminLogin />} />
			<Route path="/adminAccount" element={<AdminAccount />} />
			<Route path="/adminPharmacies" element={<AdminPharmacies />} />
			<Route path="/adminPharmacyDetails/:id" element={<AdminPharmacyDetails />} />
			<Route path="/adminMedicines" element={<AdminMedicines />} />
			<Route path="/adminMedicineDetail/:pharmacy_id/:medicine_id" element={<AdminMedicineDetail />} />
			<Route path="/pharmacyOwnerLogin" element={<OwnerLogin />} />
			<Route path="/ownerAccount" element={<OwnerAccount />} />
			<Route path="/requestPharmacy" element={<PharmacyRequest />} />
			<Route path="/requestMedicine" element={<MedicineRequest />} />
			<Route path="/customerLogin" element={<CustomerLogin />} />
			<Route path="/customerAccount" element={<CustomerAccount />} />
			<Route path="/Orders" element={<CustomerOrders />} />
			<Route path="/order_details/:order_id" element={<OrderDetails />} />
			<Route path="/pharmacyOwnerOrders" element={<OwnerPharmacyOrder />} />
			<Route path="/ownerPharmacyOrders/:pharmacy_id" element={<OwnerSpecificPharmacyOrders />} />
			<Route path="/ownerOrderDetails/:order_id" element={<OwnerOrderDetails />} />
			<Route path="/cart" element={<Cart />} />
			<Route path="/cart/shippingInfo" element={<ShippingInfo />} />
			<Route path="/orderReview" element={<OrderReview />} />
			<Route path="/payment" element={<Payment />} />
			<Route path="/success" element={<Success />} />
			<Route path="*" element={<NotFound />} />
		</Routes>
	);
}

export default App;
