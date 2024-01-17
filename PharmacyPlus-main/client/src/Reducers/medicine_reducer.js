import  {
	ALL_MEDICINE_REQUEST,
	ALL_MEDICINE_SUCCESS,
	ALL_MEDICINE_FAIL,
	MEDICINE_DETAILS_REQUEST,
	MEDICINE_DETAILS_SUCCESS,
	MEDICINE_DETAILS_FAIL,
	ADD_REVIEW_REQUEST,
	ADD_REVIEW_SUCCESS,
	ADD_REVIEW_FAIL,

	CLEAR_ERRORS
} from '../Constants/medicine_constants';

export const all_medicine_reducer = (state = {}, action) => {
	switch (action.type) {
		case ALL_MEDICINE_REQUEST:
			return {
				loading: true
			}
		case ALL_MEDICINE_SUCCESS:
			return {
				loading: false,
				medicines: action.payload.medicines
			}
		case ALL_MEDICINE_FAIL:
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

export const medicine_details_reducer = (state = {}, action) => {
	switch (action.type) {
		case MEDICINE_DETAILS_REQUEST:
			return {
				loading: true
			}
		case MEDICINE_DETAILS_SUCCESS:
			return {
				loading: false,
				medicine: action.payload.medicine
			}
		case MEDICINE_DETAILS_FAIL:
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

export const review_reducer = (state = {}, action) => {
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
}
