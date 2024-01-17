import {
	PHARMACY_APPROVAL_REQUEST,
	PHARMACY_APPROVAL_SUCCESS,
	PHARMACY_APPROVAL_FAIL,
	ADMIN_GET_ALL_MEDICINES_REQUEST,
	ADMIN_GET_ALL_MEDICINES_SUCCESS,
	ADMIN_GET_ALL_MEDICINES_FAIL,
	MEDICINE_APPROVAL_REQUEST,
	MEDICINE_APPROVAL_SUCCESS,
	MEDICINE_APPROVAL_FAIL,

	CLEAR_ERRORS,
} from '../Constants/admin_constants';

import axios from 'axios';

export const changePharmacyApproval = (id, approval) => async (dispatch) => {
	try {
		dispatch({ type: PHARMACY_APPROVAL_REQUEST });

		const config = {
			headers: {
				'Content-Type': 'application/json',
			},
		};

		const { data } = await axios.put(
			`http://localhost:4000/api/update_pharmacy_approval`,
			{ pharmacy_id: id, is_approved: approval },
			config
		);

		dispatch({
			type: PHARMACY_APPROVAL_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: PHARMACY_APPROVAL_FAIL,
			payload: error.response.data.message,
		});
	}
};

export const change_medicine_approval = (id, approval) => async (dispatch) => {
	try {
		dispatch({ type: MEDICINE_APPROVAL_REQUEST });

		const config = {
			headers: {
				'Content-Type': 'application/json',
			},
		};

		const { data } = await axios.put(
			`http://localhost:4000/api/update_medicine_approval`,
			{ medicine_id: id, is_approved: approval },
			config
		);

		dispatch({
			type: MEDICINE_APPROVAL_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: MEDICINE_APPROVAL_FAIL,
			payload: error.response.data.message,
		});
	}
};

export const get_all_medicines_admin = () => async (dispatch) => {
	try {
		dispatch({ type: ADMIN_GET_ALL_MEDICINES_REQUEST });

		const { data } = await axios.get(
			`http://localhost:4000/api/admin_get_all_medicines`
		);

		dispatch({
			type: ADMIN_GET_ALL_MEDICINES_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: ADMIN_GET_ALL_MEDICINES_FAIL,
			payload: error.response.data.message,
		});
	}
};

export const clear_errors = () => async (dispatch) => {
	dispatch({
		type: CLEAR_ERRORS
	})
};

