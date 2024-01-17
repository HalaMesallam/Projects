import {
	CUSTOMER_ORDER_REQUEST,
	CUSTOMER_ORDER_SUCCESS,
	CUSTOMER_ORDER_FAIL,
	CUSTOMER_ORDER_DETAILS_REQUEST,
	CUSTOMER_ORDER_DETAILS_SUCCESS,
	CUSTOMER_ORDER_DETAILS_FAIL,

	CLEAR_ERRORS
} from "../Constants/customer_constants";

export const customerOrderReducer = (state = {}, action) => {
	switch (action.type) {
		case CUSTOMER_ORDER_REQUEST:
			return {
				loading: true,
			};

		case CUSTOMER_ORDER_SUCCESS:
			return {
				loading: false,
				orders: action.payload,
			};

		case CUSTOMER_ORDER_FAIL:
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

export const customerOrderDetailsReducer = (state = {}, action) => {
	switch (action.type) {
		case CUSTOMER_ORDER_DETAILS_REQUEST:
			return {
				loading: true,
			};

		case CUSTOMER_ORDER_DETAILS_SUCCESS:
			return {
				loading: false,
				order: action.payload,
			};

		case CUSTOMER_ORDER_DETAILS_FAIL:
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
