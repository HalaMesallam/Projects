import {
	LOGIN_REQUEST,
	LOGIN_SUCCESS,
	LOGIN_FAIL,
} from '../Constants/login_constants';

export const login_reducer = (state = { user: {} }, action) => {
	switch (action.type) {
		case LOGIN_REQUEST:
			return {
				loading: true,
				isAuthenticated: false,
			};
		case LOGIN_SUCCESS:

			localStorage.setItem('loginInfo', JSON.stringify({...state, loading: false, isAuthenticated: true, user: action.payload}))
			return {
				...state,
				loading: false,
				isAuthenticated: true,
				user: action.payload,
			};
		case LOGIN_FAIL:
			return {
				loading: false,
				isAuthenticated: false,
				user: null,
				error: action.payload,
			};
		default:
			return state;
	}
};
