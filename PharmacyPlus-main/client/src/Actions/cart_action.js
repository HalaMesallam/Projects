import {
	ADD_TO_CART,
	REMOVE_FROM_CART,
	SAVE_SHIPPING_INFO
} from '../Constants/cart_constants';

export const addToCartAction = ({ medicine, quantity }) => async (dispatch, getState) => {
	try {
		console.log("medicine: ", medicine);
		dispatch({
			type: ADD_TO_CART,
			payload: {
				medicine_id: medicine.medicine_id,
				medicine_name: medicine.medicine_name,
				medicine_price: medicine.medicine_price,
				medicine_image: medicine.medicine_image,
				medicine_stock: medicine.medicine_stock,
				medicine_quantity: quantity,
				medicine_description: medicine.medicine_description,
				medicine_total_reviews: medicine.medicine_total_reviews,
				medicine_rating: medicine.medicine_rating,
				pharmacy_id: medicine.pharmacy_id,
			},
		});

		localStorage.setItem('cartItems', JSON.stringify(getState().cart.cartItems));
	} catch (error) {
		console.log(error);
	}
}

export const removeItemAction = (id) => async (dispatch, getState) => {
	try {
		dispatch({
			type: REMOVE_FROM_CART,
			payload: id,
		});

		localStorage.setItem('cartItems', JSON.stringify(getState().cart.cartItems));
	} catch (error) {
		console.log(error);
	}
};

export const saveShippingInfo = (data) => async (dispatch) => {
	// Dispatch all the shipping information.
	dispatch({
		type: SAVE_SHIPPING_INFO,
		payload: data
	})

	// Save the shipping information to localStorage.
	localStorage.setItem("shippingInfo", JSON.stringify(data));
}
