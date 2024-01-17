import {
	REQUEST_PHARMACY_REQUEST,
	REQUEST_PHARMACY_SUCCESS,
	REQUEST_PHARMACY_FAIL,
	RESET_PHARMACY_REQUEST_SUCCESS,
	REQUEST_MEDICINE_REQUEST,
	REQUEST_MEDICINE_SUCCESS,
	REQUEST_MEDICINE_FAIL,
	RESET_MEDICINE_REQUEST_SUCCESS,
	PHARMACY_REQUEST,
	PHARMACY_SUCCESS,
	PHARMACY_FAIL,
	OWNER_SPECIFIC_PHARMACY_ORDER_REQUEST,
	OWNER_SPECIFIC_PHARMACY_ORDER_SUCCESS,
	OWNER_SPECIFIC_PHARMACY_ORDER_FAIL,
	OWNER_ORDER_DETAILS_REQUEST,
	OWNER_ORDER_DETAILS_SUCCESS,
	OWNER_ORDER_DETAILS_FAIL,

	CLEAR_ERRORS,
} from "../Constants/owner_constants";

export const requestPharmacyReducer = (state = { pharmacy: {} }, action) => {
	switch (action.type) {
		case REQUEST_PHARMACY_REQUEST:
			return {
				loading: true,
			};
		case REQUEST_PHARMACY_SUCCESS:
			return {
				loading: false,
				success: action.payload,
			};
		case REQUEST_PHARMACY_FAIL:
			return {
				loading: false,
				error: action.payload,
			};
		case RESET_PHARMACY_REQUEST_SUCCESS:
			return {
				loading: false,
				success: false,
			};
		case CLEAR_ERRORS:
			return {
				...state,
				error: null,
			};
		default:
			return state;
	}
}

export const requestMedicineReducer = (state = { medicine: {} }, action) => {
	switch (action.type) {
		case REQUEST_MEDICINE_REQUEST:
			return {
				loading: true,
			};
		case REQUEST_MEDICINE_SUCCESS:
			return {
				loading: false,
				success: action.payload,
			};
		case REQUEST_MEDICINE_FAIL:
			return {
				loading: false,
				error: action.payload,
			};
		case RESET_MEDICINE_REQUEST_SUCCESS:
			return {
				loading: false,
				success: false,
			};
		case CLEAR_ERRORS:
			return {
				...state,
				error: null,
			};
		default:
			return state;
	}
};

export const pharmacy_owner_pharmacies = (state = {}, action) => {
	switch (action.type) {
		case PHARMACY_REQUEST:
			return {
				loading: true,
			};
		case PHARMACY_SUCCESS:
			return {
				loading: false,
				pharmacies: action.payload.pharmacies,
			};
		case PHARMACY_FAIL:
			return {
				loading: false,
				error: action.payload,
			};
		case CLEAR_ERRORS:
			return {
				...state,
				error: null,
			};
		default:
			return state;
	}
}

export const owner_get_specific_pharmacy_orders_reducer = (state = {}, action) => {
	switch (action.type) {
		case OWNER_SPECIFIC_PHARMACY_ORDER_REQUEST:
			return {
				loading: true,
			};
		case OWNER_SPECIFIC_PHARMACY_ORDER_SUCCESS:
			return {
				loading: false,
				orders: action.payload.orders,
			};
		case OWNER_SPECIFIC_PHARMACY_ORDER_FAIL:
			return {
				loading: false,
				error: action.payload,
			};
		case CLEAR_ERRORS:
			return {
				...state,
				error: null,
			};
		default:
			return state;
	}
}

export const owner_get_specific_order_details = (state = {}, action) => {
	switch (action.type) {
		case OWNER_ORDER_DETAILS_REQUEST:
			return {
				loading: true,
			};
		case OWNER_ORDER_DETAILS_SUCCESS:
			return {
				loading: false,
				order: action.payload.order,
			};
		case OWNER_ORDER_DETAILS_FAIL:
			return {
				loading: false,
				error: action.payload,
			};
		case CLEAR_ERRORS:
			return {
				...state,
				error: null,
			};
		default:
			return state;
	}
}
