import React, { Fragment, useState } from 'react';
import { toast } from 'react-toastify';
import { useDispatch } from 'react-redux';

import './Cart.css';
import { addToCartAction, removeItemAction } from '../../Actions/cart_action';

const CartItemCard = ({ item }) => {

	const dispatch = useDispatch();
	const [quantity, setQuantity] = useState(item.medicine_quantity);

	function reduceQuantity() {
		if (quantity === 1) {
			setQuantity(quantity);
		}
		else {
			const temp = quantity - 1;
			setQuantity(temp);
			dispatch(addToCartAction({ medicine: item, quantity: temp }));
		}
	}

	// Increase the quantity of item in cart.
	function addQuantity() {
		if (item.medicine_stock <= quantity) {
			setQuantity(quantity);
			toast("Only " + item.medicine_stock + " in Stock.")
		}
		else {
			const temp = quantity + 1;
			setQuantity(temp);
			dispatch(addToCartAction({ medicine: item, quantity: temp }));
		}
	}

	function removeItem() {
		dispatch(removeItemAction(item.medicine_id));
		toast.success(item.medicine_name + " Removed From Cart.")
	}

	return (
		<Fragment>
			<div className="cart-item-card-container">
				{/* Image of the product which will be the link which take to the product description */}
				<a href={"/pharmacy/medicines/" + item.pharmacy_id + "/" + item.medicine_id}>
					<img src={"/Images/Medicine/" + item.medicine_image} alt={item.medicine_name} />
				</a>
				<div className='cart-item-card-info'>
					{/* Name of the product which will be the link which take to the product description */}
					<a href={"/pharmacy/medicines/" + item.pharmacy_id + "/" + item.medicine_id}>
						<h4>{item.medicine_name}</h4>
					</a>
					{/* Product Price and Quantity buttons */}
					<h1>${item.medicine_price}</h1>
					<div className="specific-product-quantity">
						<button onClick={reduceQuantity}>-</button>
						<input type="number" value={quantity} readOnly />
						<button onClick={addQuantity}>+</button>
					</div>
					<p style={{ marginTop: '5px' }} onClick={removeItem} >Remove</p>
				</div>
			</div>
		</Fragment>
	)
}

export default CartItemCard
