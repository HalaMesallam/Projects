import React, { Fragment, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { customer_all_orders, clear_errors } from '../../Actions/customer_action';
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';
import OrderCard from './OrderCard.js';

import { toast } from 'react-toastify';

const CustomerOrders = () => {

	const navigate = useNavigate();
	const { user, loading, isAuthenticated } = useSelector(state => state.userLogin);
	const { loading: orderLoading, error, orders } = useSelector(state => state.customer_order);

	const dispatch = useDispatch();

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated) {
				navigate("/customerLogin");
			} else {
				if (!user.is_customer) {
					toast("You cannot access Customer Order.")
					navigate("/");
				}
			}
		}

		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}

		if (user) {
			dispatch(customer_all_orders(user.customer[0].customer_id));
		}

	}, [isAuthenticated, loading, navigate, user, dispatch, error]);

	return (
		<Fragment>
			{orderLoading || loading ? <Loading /> : orders && (
				<Fragment>
					<MetaData title={'Customer Orders'} />
					{orders.orders && orders.orders.length <= 0 ? (
						<Fragment>
							<h1 className="text-center">No Orders</h1>
						</Fragment>
					) : (
						<Fragment>
							<h1 style={{ textAlign: "center" }}>Your Orders</h1>
							{orders.orders && orders.orders.map(order => (
								<OrderCard key={order.order_id} order={order} />
							))}
						</Fragment>
					)}
				</Fragment>
			)}
		</Fragment>
	)
}

export default CustomerOrders;
