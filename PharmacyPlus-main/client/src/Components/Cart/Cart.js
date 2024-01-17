import React, { Fragment, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { toast } from 'react-toastify';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import './Cart.css';
import CartItemCard from './CartItemCard.js';

const Cart = () => {

	const navigate = useNavigate();
	const { user, isAuthenticated, loading } = useSelector(state => state.userLogin);
	const { cartItems } = useSelector(state => state.cart);

	let total = 0;

	for (let i = 0; i < cartItems.length; i++) {
		total += cartItems[i].medicine_price * cartItems[i].medicine_quantity;
	}

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated || !user.is_customer) {
				toast.error('You are not authorized to access this page');
				navigate('/customerLogin');
			}
		}
	}, [loading, isAuthenticated, user, navigate]);

	return (
		<Fragment>
			<MetaData title={'Cart'} />
			{loading ? <Loading /> : (
				<Fragment>
					<div className="cart-container">
						<h2>Shopping Cart</h2>
						<hr />
						{/* If cart is empty then show this. */}
						{cartItems.length === 0 && <div className="cart-empty">
							<p>No Items In Cart.</p>
							<a href="/" className="view-product-btn"> View Pharmacies </a>
						</div>}
						{/* If cart is not empty show every item as CartItemCard. */}
						{cartItems && cartItems.map((item) => <CartItemCard key={item.medicine_id} item={item} />)}
						<div className="cart-total-price">
							<hr />
							{/* Subtotal and Link to Checkout. */}
							<h3>Subtotal ({cartItems.length} item): <span>${total.toFixed(2)}</span></h3>
							<a href="/cart/shippingInfo"><button className="proceed-checkout" disabled={total === 0}>Proceed to Checkout</button></a>
						</div>
					</div>
				</Fragment>
			)}
		</Fragment>
	)
}

export default Cart
