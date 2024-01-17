import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';
import OwnerPharmacyOrdersCard from './OwnerPharmacyOrdersCard.js';

import { toast } from 'react-toastify';

import { owner_pharmacy_requests, clear_errors as pharmacy_clear_error } from '../../Actions/owner_action';
import { clear_errors } from '../../Actions/login_action';

const OwnerPharmacyOrders = () => {

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const { user, loading, isAuthenticated, error } = useSelector(state => state.userLogin);
	const { loading: loadingPharmacies, error: pharmacyErrors, pharmacies } = useSelector(state => state.owner_pharmacies);

	const owner_id = user && user.owner[0].owner_id;

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated || !user || !user.is_owner) {
				navigate('/');
			}
		}

		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}

		if (pharmacyErrors) {
			toast.error(pharmacyErrors);
			dispatch(pharmacy_clear_error());
		}

		if (user) {
			dispatch(owner_pharmacy_requests(owner_id));
		}

	}, [loading, navigate, user, owner_id, isAuthenticated, error, dispatch, pharmacyErrors]);

	return (
		<Fragment>
			{loading || loadingPharmacies ? <Loading /> : (
				<Fragment>
					<MetaData title={'Owner Pharmacies'} />
					<div className="pharmacy_container">
						{pharmacies && pharmacies.map(pharmacy => (
							<OwnerPharmacyOrdersCard key={pharmacy.pharmacy_id} pharmacy={pharmacy} />
						))}
					</div>
				</Fragment>
			)}
		</Fragment>
	)
}

export default OwnerPharmacyOrders
