import {
	REQUEST_PHARMACY_REQUEST,
	REQUEST_PHARMACY_SUCCESS,
	REQUEST_PHARMACY_FAIL,
	REQUEST_MEDICINE_REQUEST,
	REQUEST_MEDICINE_SUCCESS,
	REQUEST_MEDICINE_FAIL,
	PHARMACY_REQUEST,
	PHARMACY_SUCCESS,
	PHARMACY_FAIL,
	OWNER_SPECIFIC_PHARMACY_ORDER_REQUEST,
	OWNER_SPECIFIC_PHARMACY_ORDER_SUCCESS,
	OWNER_SPECIFIC_PHARMACY_ORDER_FAIL,
	OWNER_ORDER_DETAILS_REQUEST,
	OWNER_ORDER_DETAILS_SUCCESS,
	OWNER_ORDER_DETAILS_FAIL,

	CLEAR_ERRORS
} from '../Constants/owner_constants';

import axios from 'axios';

export const requestPharmacy = (pharmacyData) => async (dispatch) => {
	try {
		dispatch({ type: REQUEST_PHARMACY_REQUEST });

		const config = {
			headers: {
				'Content-Type': 'application/json'
			}
		}
		
		const { data } = await axios.post(
			`http://localhost:4000/api/request_pharmacy`,
			pharmacyData,
			config
		);

		dispatch({
			type: REQUEST_PHARMACY_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: REQUEST_PHARMACY_FAIL,
			payload: error.response.data.message,
		});
	}
};

export const requestMedicine = (medicineData) => async (dispatch) => {
	try {
		dispatch({ type: REQUEST_MEDICINE_REQUEST });

		const config = {
			headers: {
				'Content-Type': 'application/json'
			}
		}

		const { data } = await axios.post(
			`http://localhost:4000/api/request_medicine`,
			medicineData,
			config
		);

		dispatch({
			type: REQUEST_MEDICINE_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: REQUEST_MEDICINE_FAIL,
			payload: error.response.data.message,
		});
	}
};

export const owner_pharmacy_requests = (id) => async (dispatch) => {
	try {
		dispatch({ type: PHARMACY_REQUEST });

		const { data } = await axios.get(
			`http://localhost:4000/api/owner_pharmacy_requests/${id}`,
		);

		dispatch({
			type: PHARMACY_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: PHARMACY_FAIL,
			payload: error.response.data.message,
		});
	}
}

export const owner_get_specific_pharmacy_order = (id) => async (dispatch) => {
	try {
		dispatch({ type: OWNER_SPECIFIC_PHARMACY_ORDER_REQUEST });

		const { data } = await axios.get(
			`http://localhost:4000/api/owner_get_specific_pharmacy_order/${id}`,
		);

		dispatch({
			type: OWNER_SPECIFIC_PHARMACY_ORDER_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: OWNER_SPECIFIC_PHARMACY_ORDER_FAIL,
			payload: error.response.data.message,
		});
	}
}

export const owner_order_details = (id) => async (dispatch) => {
	try {
		dispatch({ type: OWNER_ORDER_DETAILS_REQUEST });

		const { data } = await axios.get(
			`http://localhost:4000/api/owner_order_details/${id}`,
		);

		dispatch({
			type: OWNER_ORDER_DETAILS_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: OWNER_ORDER_DETAILS_FAIL,
			payload: error.response.data.message,
		});
	}
}

export const clear_errors = () => async (dispatch) => {
	dispatch({
		type: CLEAR_ERRORS
	})
};
