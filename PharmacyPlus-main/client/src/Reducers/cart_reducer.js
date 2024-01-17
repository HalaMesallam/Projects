import {
	ADD_TO_CART,
	REMOVE_FROM_CART,
	SAVE_SHIPPING_INFO
} from '../Constants/cart_constants';

import { toast } from 'react-toastify';

export const cartReducer = (state = { cartItems: [] }, action) => {
	switch (action.type) {
		case ADD_TO_CART:
			const item = action.payload;
			const existItem = state.cartItems.find(
				(x) => x.medicine_id === item.medicine_id
			)

			const pharmacy_diff = state.cartItems.find((x) => x.pharmacy_id !== item.pharmacy_id);

			if (pharmacy_diff) {
				toast.error('You can only add medicines from one pharmacy at a time');
				return {
					...state,
				};
			}
			else if (existItem) {
				toast.success("Medicine added to cart.");
				return {
					...state,
					cartItems: state.cartItems.map((x) => x.medicine_id === existItem.medicine_id ? item : x)
				}
			} else {
				toast.success("Medicine added to cart.");
				return {
					...state,
					cartItems: [...state.cartItems, item],
				};
			}
		default:
			return state;

		case REMOVE_FROM_CART:
			return {
				...state,
				cartItems: state.cartItems.filter((x) => x.medicine_id !== action.payload),
			};
		case SAVE_SHIPPING_INFO:
			return {
				...state,
				shippingInfo: action.payload,
			};
	}
}
