import React, { Fragment, useEffect, useState } from 'react';

import { toast } from 'react-toastify';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import AbcIcon from '@mui/icons-material/Abc';
import HomeIcon from '@mui/icons-material/Home';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import { saveShippingInfo } from '../../Actions/cart_action';

import './ShippingInformation.css';

const ShippingInfo = () => {

	const navigate = useNavigate();
	const dispatch = useDispatch();

	const { user, isAuthenticated, loading } = useSelector(state => state.userLogin);
	const { cartItems, shippingInfo } = useSelector(state => state.cart);

	const [address, setAddress] = useState(shippingInfo.address);
	const [name, setName] = useState(shippingInfo.name)

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

	}, [loading, isAuthenticated, user, navigate, cartItems]);

	const shippingInfoSubmit = (e) => {
		e.preventDefault();

		dispatch(saveShippingInfo({ address, name }));
		navigate('/orderReview');
	}

	return (
		<Fragment>
			<MetaData title={'Shipping Information'} />
			{loading ? <Loading /> : (
				<Fragment>
					<div className="Shipping-info-container">
						<div className="Shipping-info-width">
							{/* Heading of page. */}
							<h2>Shipping Information</h2>
							{/* Creating Form which user can submit with every information. */}
							<form className="shipping-info" encType='multipart/form-data' onSubmit={shippingInfoSubmit}>
								{/* First Name */}
								<div className="first-name-shipping">
									<h5>Name of Person to Pickup Delivery: </h5>
									<AbcIcon />
									<input type="text" placeholder="Name" required value={name} onChange={(event) => setName(event.target.value)} />
								</div>
								{/* Address */}
								<div className="address-shipping">
									<h5> Address: </h5>
									<HomeIcon />
									<input type="text" placeholder="Address" required value={address} onChange={(event) => setAddress(event.target.value)} />
								</div>
								{/* Continue to checkout button. */}
								<div className="submit-button-shipping-info">
									<button type="submit">Continue</button>
								</div>

							</form>
						</div>
					</div>
				</Fragment>
			)}
		</Fragment>
	)
}

export default ShippingInfo
