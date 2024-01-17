import {
	ADMIN_GET_ALL_MEDICINES_REQUEST,
	ADMIN_GET_ALL_MEDICINES_SUCCESS,
	ADMIN_GET_ALL_MEDICINES_FAIL,

	CLEAR_ERRORS
} from "../Constants/admin_constants";


export const get_all_medicines_reducer = (state = {}, action) => {
	switch (action.type) {
		case ADMIN_GET_ALL_MEDICINES_REQUEST:
			return {
				loading: true,
			};
		case ADMIN_GET_ALL_MEDICINES_SUCCESS:
			return {
				loading: false,
				medicines: action.payload.medicines,
			};
		case ADMIN_GET_ALL_MEDICINES_FAIL:
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
};


