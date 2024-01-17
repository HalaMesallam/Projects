import React, { Fragment, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { toast } from 'react-toastify';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import CartItemCard from '../Cart/CartItemCard';

import './OrderReview.css';

const OrderReview = () => {

	const navigate = useNavigate();

	window.paid = false;

	const { cartItems, shippingInfo } = useSelector(state => state.cart);
	const { user, isAuthenticated, loading } = useSelector(state => state.userLogin);

	let total = 0;

	for (let i = 0; i < cartItems.length; i++) {
		total += cartItems[i].medicine_price * cartItems[i].medicine_quantity;
	}

	let tax = total * 0.05;

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated || !user.is_customer) {
				toast.error('You are not authorized to access this page');
				navigate('/customerLogin');
			}
		}

		if (cartItems.length === 0) {
			navigate('/cart');
		}

		if (!shippingInfo) {
			navigate('/cart/shippingInfo');
		}

	}, [loading, isAuthenticated, user, navigate, cartItems, shippingInfo]);

	return (
		<Fragment>
			<MetaData title={'Order Review'} />
			{loading ? <Loading /> : (
				<Fragment>
					<div className='order-review-container'>
						<div className='shipping-information-review'>
							{/* two divs, first contain shipping information. */}
							<h2>Shipping Information</h2>
							<div className='all-shipping-info'>
								{/* Name */}
								<div className='info'>
									<h5>Delivery Person: </h5>
									<h5> {shippingInfo.name}</h5>
								</div>
								{/* Address to ship */}
								<div className='info'>
									<h5>Address: </h5>
									<h5>{shippingInfo.address} </h5>
								</div>
							</div>
						</div>
						{/* All order summery. */}
						<div className='order-summery'>
							<h3>Order Summery </h3>
							<div className='all-order-info'>
								{/* Subtotal of the cart. */}
								<div className='subtotal-confirm'>
									<h5>Subtotal ({cartItems.length} item): </h5>
									<span>${total.toFixed(2)}</span>
								</div>
								{/* Total GST */}
								<div className='info'>
									<h5> Tax (5%): </h5>
									<h5>${tax.toFixed(2)}</h5>
								</div>
								{/* Total of the cart. */}
								<div className='info'>
									<h5> Total: </h5>
									<h5>${(parseFloat(total) + parseFloat(tax)).toFixed(2)}</h5>
								</div>
								{/* Button to proceed to payment. */}
								<a href="/payment" className='payment'><button>Proceed to Payment</button></a>
							</div>
						</div>
					</div>
					{/* Total items in the cart. */}
					<div className='cart-container' style={{marginTop: "8vmax"}}>
						<h2>Cart Items</h2>
						{cartItems && cartItems.map((item) => <CartItemCard key={item.medicine_id} item={item} />)}
					</div>
				</Fragment>
			)}
		</Fragment>
	)
}

export default OrderReview
