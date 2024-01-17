import React, { Fragment, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { toast } from 'react-toastify';

import { useNavigate, useParams } from 'react-router-dom';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import { clear_errors, owner_get_specific_pharmacy_order } from '../../Actions/owner_action';

const OwnerSpecificPharmacyOrders = () => {

	const { pharmacy_id } = useParams();
	const dispatch = useDispatch();
	const navigate = useNavigate();

	const { orders, loading: order_loading, error: order_error } = useSelector(state => state.owner_specific_pharmacy_orders);
	const { isAuthenticated, user, loading } = useSelector(state => state.userLogin);

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated || !user || !user.is_owner) {
				navigate('/');
			}
		}

		if (order_error) {
			toast.error(order_error);
			dispatch(clear_errors());
		}

		if (user) {
			dispatch(owner_get_specific_pharmacy_order(pharmacy_id));
		}
	}, [navigate, loading, user, order_error, isAuthenticated, dispatch, pharmacy_id]);

	return (
		<Fragment>
			{order_loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'Owner Specific Pharmacy Orders'} />
					{orders && orders.length > 0 ? (
						<Fragment>
							<h1 className="text-center">Orders</h1>
							{orders.map(order => (
								<Fragment key={order.order_id}>
									<div className='order-container-info'>
										<div className='order-detail-container'>
											<div className='order-information'>
												<div>
													{/* Total of the order */}
													<div className='order-info'>
														<p>TOTAL</p>
														<p>{parseFloat(order.total_price).toFixed(2)}</p>
													</div>
													<div className='order-info'>
														<p>ITEMS TOTAL</p>
														<p>{(parseFloat(order.total_price).toFixed(2) - parseFloat(order.tax_price).toFixed(2)).toFixed(2)}</p>
													</div>
													<div className='order-info'>
														<p>TOTAL TAX</p>
														<p>{(order.tax_price).toFixed(2)}</p>
													</div>
													{/* Ship to name of the person */}
													<div className='order-info'>
														<p>SHIP TO</p>
														<p>{order.customer_name} <br /> {order.order_address}</p>
													</div>
													<div className='order-info'>
														<p>PHARMACY NAME</p>
														<p>{order.pharmacy_name}</p>
													</div>
												</div>
											</div>
											{/* Order Number and link to view details of order */}
											<div className='order-number'>
												<p>ORDER # {order.order_id}</p>
												<p>PHARMACY # {order.pharmacy_id}</p>
												<a href={"/ownerOrderDetails/" + order.order_id}>View Order Details</a>
											</div>
										</div>
									</div>
								</Fragment>
							))}
						</Fragment>) : (
						<Fragment>
							<h1 className="text-center">No Orders</h1>
						</Fragment>
					)}
				</Fragment>
			)}
		</Fragment>
	)
}

export default OwnerSpecificPharmacyOrders
