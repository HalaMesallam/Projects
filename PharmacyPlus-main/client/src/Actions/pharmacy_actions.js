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

import axios from "axios";

// Get all pharmacies
export const get_all_pharmacies = () => async (dispatch) => {
	try {
		dispatch({ type: ALL_PHARMACY_REQUEST });

		const { data } = await axios.get("http://localhost:4000/api/all_pharmacies");

		dispatch({
			type: ALL_PHARMACY_SUCCESS,
			payload: data
		});
	} catch (error) {
		dispatch({
			type: ALL_PHARMACY_FAIL,
			payload: error.response.data.message
		});
	}
};

export const pharmacy_details = (id) => async (dispatch) => {
	try {
		dispatch({ type: PHARMACY_DETAILS_REQUEST });

		const { data } = await axios.get("http://localhost:4000/api/pharmacy_details/" + id);

		dispatch({
			type: PHARMACY_DETAILS_SUCCESS,
			payload: data
		});
	} catch (error) {
		dispatch({
			type: PHARMACY_DETAILS_FAIL,
			payload: error.response.data.message
		});
	}
};

export const add_pharmacy_review = (id, rating) => async (dispatch) => {
	try {
		dispatch({ type: ADD_REVIEW_REQUEST });

		const { data } = await axios.put("http://localhost:4000/api/add_pharmacy_review/" + id, { rating });

		dispatch({
			type: ADD_REVIEW_SUCCESS,
			payload: data.success
		});
	} catch (error) {
		dispatch({
			type: ADD_REVIEW_FAIL,
			payload: error.response.data.message
		});
	}
};

export const clearErrors = () => async (dispatch) => {
	dispatch({ type: CLEAR_ERRORS });
};
