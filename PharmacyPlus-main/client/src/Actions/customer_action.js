import {
	CUSTOMER_ORDER_REQUEST,
	CUSTOMER_ORDER_SUCCESS,
	CUSTOMER_ORDER_FAIL,
	CUSTOMER_ORDER_DETAILS_REQUEST,
	CUSTOMER_ORDER_DETAILS_SUCCESS,
	CUSTOMER_ORDER_DETAILS_FAIL,

	CLEAR_ERRORS
} from "../Constants/customer_constants";

import axios from "axios";

export const customer_all_orders = (id) => async (dispatch) => {
	try {
		dispatch({ type: CUSTOMER_ORDER_REQUEST });

		const { data } = await axios.get(
			`http://localhost:4000/api/customer_orders/${id}`
		);

		dispatch({
			type: CUSTOMER_ORDER_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: CUSTOMER_ORDER_FAIL,
			payload: error.response.data.message,
		});
	}
}

export const customer_order_details = (id) => async (dispatch) => {
	try {
		dispatch({ type: CUSTOMER_ORDER_DETAILS_REQUEST });

		const { data } = await axios.get(
			`http://localhost:4000/api/customer_order_details/${id}`
		);

		dispatch({
			type: CUSTOMER_ORDER_DETAILS_SUCCESS,
			payload: data,
		});
	} catch (error) {
		dispatch({
			type: CUSTOMER_ORDER_DETAILS_FAIL,
			payload: error.response.data.message,
		});
	}
}

export const clear_errors = () => async (dispatch) => {
	dispatch({
		type: CLEAR_ERRORS,
	});
};
