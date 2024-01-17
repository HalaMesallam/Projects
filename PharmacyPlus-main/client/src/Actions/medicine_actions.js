import {
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

import axios from 'axios';

// Get all medicines for pharmacy id

export const get_all_medicines = (id) => async (dispatch) => {
	try {
		dispatch({ type: ALL_MEDICINE_REQUEST });

		const { data } = await axios.get("http://localhost:4000/api/all_medicines/" + id);

		dispatch({
			type: ALL_MEDICINE_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: ALL_MEDICINE_FAIL,
			payload: error.response.data.message
		})
	}
};

// get specific medicine details
export const medicine_details = (pharmacy_id, medicine_id) => async (dispatch) => {
	try {
		dispatch({ type: MEDICINE_DETAILS_REQUEST });

		const { data } = await axios.get("http://localhost:4000/api/medicine/" + pharmacy_id + "/" + medicine_id);
		
		dispatch({
			type: MEDICINE_DETAILS_SUCCESS,
			payload: data
		})
	} catch (error) {
		dispatch({
			type: MEDICINE_DETAILS_FAIL,
			payload: error.response.data.message
		})
	}
};

export const clear_errors = () => async (dispatch) => {
	dispatch({
		type: CLEAR_ERRORS
	})
};

export const addReview = (pharmacy_id, medicine_id, rating) => async (dispatch) => {
	try {
		dispatch({ type: ADD_REVIEW_REQUEST });

		const config = {
			headers: {
				'Content-Type': 'application/json'
			}
		}

		const { data } = await axios.put("http://localhost:4000/api/medicine/update_review/" + pharmacy_id + "/" + medicine_id, { rating }, config);

		dispatch({
			type: ADD_REVIEW_SUCCESS,
			payload: data.success
		})
	} catch (error) {
		dispatch({
			type: ADD_REVIEW_FAIL,
			payload: error.response.data.message
		})
	}
}
