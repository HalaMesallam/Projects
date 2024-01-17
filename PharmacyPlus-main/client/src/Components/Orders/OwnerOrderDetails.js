import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import { clear_errors, owner_order_details } from "../../Actions/owner_action";

import { toast } from 'react-toastify';

import "./OwnerOrderDetails.css";

const OwnerOrderDetails = () => {

	const { order_id } = useParams();
	const navigate = useNavigate();
	const dispatch = useDispatch();

	const { user, loading, isAuthenticated } = useSelector(state => state.userLogin);
	const { order, loading: loadingOrder, error: orderError } = useSelector(state => state.owner_order_details);

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated || !user || !user.is_owner) {
				navigate('/');
			}

			if (orderError) {
				toast.error(orderError);
				dispatch(clear_errors());
			}

			if (user) {
				dispatch(owner_order_details(order_id));
			}
		}
	}, [loading, navigate, user, isAuthenticated, orderError, dispatch, order_id]);

	return (
		<Fragment>
			{loadingOrder ? <Loading /> : (
				<Fragment>
					<MetaData title="Order Details" />
					{order && order.length > 0 && (
						<div className='specific-order-container'>
							{/* Contain the heading and Order Number */}
							<div className='order-details-heading'>
								<h2>Order Details</h2>
								<div className='order-detail-number'>
									<div>
										{/* Order Number. */}
										Order # {order[0].order_id}
									</div>
								</div>
							</div>
							{/* Div Contain Shipping Information and Order Summery */}
							<div className='specific-order-detail-page'>
								<div className='order-details'>
									{/* Shipping Information */}
									{/* Wait till shippingInfo is available and then show it. */}
									<div className='order-shipping-info'>
										<p>Shipping Address</p>
										<p>{order[0].customer_name}</p>
										<p>{order[0].order_address}</p>
									</div>

									{/* Order Summery */}
									<div className='order-detail-summery'>
										<p>Order Summery</p>
										<p>Item(s) Subtotal: <span>${(parseFloat(order[0].total_price).toFixed(2) - parseFloat(order[0].tax_price).toFixed(2)).toFixed(2)}</span></p>
										<p>Tax: <span>${(order[0].tax_price).toFixed(2)}</span></p>
										<p>Grand Total: <span>${(order[0].total_price).toFixed(2)}</span></p>
									</div>
								</div>
							</div>
							{/* Div Containing Items Component. */}
							{order.map(order =>
								<div div className='specific-order-items' >
									<div className='order-product-info'>
										{/* Item Image and link to product */}
										<a href={"/pharmacy/medicines/" + order.pharmacy_id + "/" + order.medicine_id}>
											<img src={"/Images/Medicine/" + order.medicine_image} alt={order.medicine_name} />
										</a>
										{/* Item information */}
										<div className='order-item-info'>
											{/* Order item name with link to product */}
											<a href={"/pharmacy/medicines/" + order.pharmacy_id + "/" + order.medicine_id}>
												<h4>
													{order.medicine_name}
												</h4>
											</a>
											{/* Item price and Quantity of ordered item. */}
											<h1>${(order.medicine_price).toFixed(2)}</h1>
											<p>QTY: {order.quantity}</p>
										</div>
									</div>
									{/* Line for design */}
									< hr style={{ marginBottom: "1.5vmax", width: "98%" }} />
								</div>
							)}
						</div>
					)}
				</Fragment>
			)}
		</Fragment >
	)
}

export default OwnerOrderDetails
