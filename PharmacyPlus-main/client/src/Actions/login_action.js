import {
	LOGIN_REQUEST,
	LOGIN_SUCCESS,
	LOGIN_FAIL,

	CLEAR_ERRORS,
} from '../Constants/login_constants';

import axios from 'axios';

export const admin_login_action = (email, password) => async (dispatch) => {
	try {
		dispatch({ type: LOGIN_REQUEST });

		const { data } = await axios.post("http://localhost:4000/api/login_admin", {
			email,
			password
		});

		dispatch({
			type: LOGIN_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: LOGIN_FAIL,
			payload: error.response.data.message
		})
	}
};

export const pharmacy_owner_login_action = (email, password) => async (dispatch) => {
	try {
		dispatch({ type: LOGIN_REQUEST });

		const { data } = await axios.post("http://localhost:4000/api/login_pharmacy_owner", {
			email,
			password
		});

		dispatch({
			type: LOGIN_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: LOGIN_FAIL,
			payload: error.response.data.message
		})
	}
};

export const register_pharmacy_owner = (name, email, password) => async (dispatch) => {
	try {
		dispatch({ type: LOGIN_REQUEST });

		const { data } = await axios.post("http://localhost:4000/api/register_pharmacy_owner", {
			name,
			email,
			password
		});

		dispatch({
			type: LOGIN_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: LOGIN_FAIL,
			payload: error.response.data.message
		})
	}
};

export const customer_login_action = (email, password) => async (dispatch) => {
	try {
		dispatch({ type: LOGIN_REQUEST });

		const { data } = await axios.post("http://localhost:4000/api/login_customer", {
			email,
			password
		});

		dispatch({
			type: LOGIN_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: LOGIN_FAIL,
			payload: error.response.data.message
		})
	}
}

export const customer_register_action = (name, email, password, address, phone) => async (dispatch) => {
	try {
		dispatch({ type: LOGIN_REQUEST });
		
		const { data } = await axios.post("http://localhost:4000/api/register_customer", {
			name,
			email,
			password,
			address,
			phone
		});

		dispatch({
			type: LOGIN_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: LOGIN_FAIL,
			payload: error.response.data.message
		})
	}
}

export const clear_errors = () => async (dispatch) => {
	dispatch({
		type: CLEAR_ERRORS
	})
};
