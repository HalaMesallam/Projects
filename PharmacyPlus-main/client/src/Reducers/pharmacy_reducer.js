import { 
	ALL_PHARMACY_REQUEST, 
	ALL_PHARMACY_SUCCESS, 
	ALL_PHARMACY_FAIL,
	PHARMACY_DETAILS_REQUEST,
	PHARMACY_DETAILS_SUCCESS,
	PHARMACY_DETAILS_FAIL,
	ADD_REVIEW_REQUEST,
	ADD_REVIEW_SUCCESS,
	ADD_REVIEW_FAIL,
	CLEAR_ERRORS
} from "../Constants/pharmacy_constants";

// Get all pharmacies
export const all_pharmacy_reducer = (state = {}, action) => {
	switch (action.type) {
		case ALL_PHARMACY_REQUEST:
			return {
				loading: true
			}
		case ALL_PHARMACY_SUCCESS:
			return {
				loading: false,
				pharmacies: action.payload.pharmacies
			}
		case ALL_PHARMACY_FAIL:
			return {
				loading: false,
				error: action.payload
			}
		case CLEAR_ERRORS:
			return {
				...state,
				error: null
			}
		default:
			return state;
	}
};

// Get pharmacy details
export const pharmacy_details_reducer = (state = {}, action) => {
	switch (action.type) {
		case PHARMACY_DETAILS_REQUEST:
			return {
				loading: true
			}
		case PHARMACY_DETAILS_SUCCESS:
			return {
				loading: false,
				pharmacy: action.payload.pharmacy
			}
		case PHARMACY_DETAILS_FAIL:
			return {
				loading: false,
				error: action.payload
			}
		case CLEAR_ERRORS:
			return {
				...state,
				error: null
			}
		default:
			return state;
	}
}

export const add_pharmacy_review_reducer = (state = {}, action) => {
	switch (action.type) {
		case ADD_REVIEW_REQUEST:
			return {
				loading: true
			}
		case ADD_REVIEW_SUCCESS:
			return {
				loading: false,
				success: action.payload
			}
		case ADD_REVIEW_FAIL:
			return {
				loading: false,
				error: action.payload
			}
		case CLEAR_ERRORS:
			return {
				...state,
				error: null
			}
		default:
			return state;
	}
};
