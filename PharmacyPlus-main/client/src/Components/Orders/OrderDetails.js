import React, { Fragment, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { clear_errors, customer_order_details } from "../../Actions/customer_action";
import Loading from "../../Components/Layouts/Loading/loading";
import MetaData from '../Layouts/Metadata/metadata';
import OrderDetailsCard from './OrderDetailsCard.js';

import { toast } from 'react-toastify';

const OrderDetails = () => {

	const { order_id } = useParams();

	const navigate = useNavigate();
	const dispatch = useDispatch();
	const { user, loading, isAuthenticated } = useSelector(state => state.userLogin);
	const { order, loading: orderLoading, error } = useSelector(state => state.customer_order_details);

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated) {
				navigate("/");
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
			dispatch(customer_order_details(order_id));
		}

	}, [isAuthenticated, loading, navigate, user, order_id, dispatch, error]);

	return (
		<Fragment>
			{orderLoading || loading ? <Loading /> : (
				<Fragment>
					<MetaData title="Order Details" />
					{order && order.order.map(order => (
						<OrderDetailsCard key={order.medicine_id} medicine={order} />
					))}
				</Fragment>
			)}
		</Fragment>
	)
}

export default OrderDetails
