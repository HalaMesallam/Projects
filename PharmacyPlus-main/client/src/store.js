import { createStore, combineReducers, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import { get_all_medicines_reducer } from './Reducers/admin_reducer';
import { cartReducer } from './Reducers/cart_reducer';
import { customerOrderDetailsReducer, customerOrderReducer } from './Reducers/customer_reducer';
import { login_reducer } from './Reducers/login_reducer';
import { all_medicine_reducer, medicine_details_reducer, review_reducer } from './Reducers/medicine_reducer';
import { owner_get_specific_order_details, owner_get_specific_pharmacy_orders_reducer, pharmacy_owner_pharmacies, requestMedicineReducer, requestPharmacyReducer } from './Reducers/owner_reducer';

import { add_pharmacy_review_reducer, all_pharmacy_reducer, pharmacy_details_reducer } from './Reducers/pharmacy_reducer';

const reducers = combineReducers({
	// Add reducers here
	allPharmacies: all_pharmacy_reducer,
	allMedicine: all_medicine_reducer,
	medicine_details: medicine_details_reducer,
	pharmacy_details: pharmacy_details_reducer,
	newReview: review_reducer,
	newPharmacyReview: add_pharmacy_review_reducer,
	userLogin: login_reducer,
	admin_medicines: get_all_medicines_reducer,
	request_pharmacy: requestPharmacyReducer,
	request_medicine: requestMedicineReducer,
	customer_order: customerOrderReducer,
	customer_order_details: customerOrderDetailsReducer,
	owner_pharmacies: pharmacy_owner_pharmacies,
	owner_specific_pharmacy_orders: owner_get_specific_pharmacy_orders_reducer,
	owner_order_details: owner_get_specific_order_details,
	cart: cartReducer
});

let initialState = {
	userLogin: {
		user: localStorage.getItem('loginInfo') ?
			JSON.parse(localStorage.getItem('loginInfo')).user : null,
		isAuthenticated: localStorage.getItem('loginInfo') ?
			JSON.parse(localStorage.getItem('loginInfo')).isAuthenticated : false,
		loading: false,
		error: null
	},
	cart: {
		cartItems: localStorage.getItem('cartItems') ?
			JSON.parse(localStorage.getItem('cartItems')) : [],
		shippingInfo: localStorage.getItem('shippingInfo') ?
			JSON.parse(localStorage.getItem('shippingInfo')) : {}
	}
}

const middleware = [thunk];

const store = createStore(reducers, initialState, applyMiddleware(...middleware));

export default store;
