import React, { Fragment, useEffect, useRef } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import CreditCardIcon from '@mui/icons-material/CreditCard';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import DateRangeIcon from '@mui/icons-material/DateRange';

import { toast } from 'react-toastify';
import axios from 'axios';
import './Payment.css';

const Payment = () => {

	const navigate = useNavigate();

	const PaymentButton = useRef(null);

	const { isAuthenticated, loading, user } = useSelector(state => state.userLogin);
	const { cartItems, shippingInfo } = useSelector(state => state.cart);

	let subTotal = 0;

	// Calculating total of the cart.
	for (let i = 0; i < cartItems.length; i++) {
		subTotal += cartItems[i].medicine_price * cartItems[i].medicine_quantity;
	}

	let tax = subTotal * 0.05;
	let total = subTotal + tax;

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

	const paymentSubmitted = async (e) => {
		e.preventDefault();

		const order = {
			shippingInfo,
			cartItems,
			subTotal,
			tax,
			total,
			user
		}

		PaymentButton.current.disabled = true;

		try {
			const config = {
				headers: {
					'Content-Type': 'application/json'
				}
			}

			const { data } = await axios.post('http://localhost:4000/api/orders/newOrder', order, config);

			if (data.success) {
				localStorage.setItem("cartItems", []);
				localStorage.setItem("shippingInfo", JSON.stringify({}));
				window.paid = true;
				navigate('/success');
			}
			else {
				toast.error(data.message);
				PaymentButton.current.disabled = false;
			}
		}
		catch (error) {
			toast.error(error.response.data.message);
			PaymentButton.current.disabled = false;
		}

		PaymentButton.current.disabled = false;
	}

	return (
		<Fragment>
			<MetaData title={'Payment'} />
			{loading ? <Loading /> : (
				<Fragment>
					<form className='card-info-form' onSubmit={paymentSubmitted}>

						{/* Heading of the page. */}
						<h2>Card Information</h2>
						{/* Container which have all the information of the page. */}
						<div className='card-info-container'>
							{/* Card Number */}
							<div className='icon-detail'>
								<CreditCardIcon />
								<input type='text' placeholder='Card Number' required maxLength={16} />
							</div>
							{/* Expiry date */}
							<div className='icon-detail'>
								<DateRangeIcon />
								<input type='text' placeholder='MM/YY' required maxLength={5} />
							</div>
							{/* CVC of the card */}
							<div className='icon-detail'>
								<VpnKeyIcon />
								<input type='text' placeholder='CVC' required maxLength={3}/>
							</div>
							{/* Button to process. */}
							<button type='submit' ref={PaymentButton} >Pay - ${total.toFixed(2)}</button>
						</div>
					</form>
				</Fragment>
			)}
		</Fragment>
	)
}

export default Payment

