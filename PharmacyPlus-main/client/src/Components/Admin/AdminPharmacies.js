import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { toast } from 'react-toastify';

import '../Home/Home.css';
import { get_all_pharmacies, clearErrors } from '../../Actions/pharmacy_actions';
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';
import AdminPharmacyCard from './AdminPharmacyCard.js';

const AdminPharmacies = () => {

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const { pharmacies, error, loading } = useSelector(state => state.allPharmacies);
	const { loading: userLoading, isAuthenticated, user } = useSelector(state => state.userLogin);

	useEffect(() => {

		if (!userLoading) {
			if (!isAuthenticated || !user || !user.is_admin) {
				navigate('/adminLogin');
			}
		}

		if (error) {
			toast.error(error);
			dispatch(clearErrors());
		}

		if (user) {
			dispatch(get_all_pharmacies());
		}

	}, [dispatch, error, isAuthenticated, navigate, user, userLoading]);

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'Admin - All Pharmacies'} />
					<h1 style={{textAlign: "center"}}>Not Approved</h1>
					<div className="pharmacy_container">
						{pharmacies && pharmacies.map(pharmacy => (
							!pharmacy.is_approved ? (
								<AdminPharmacyCard key={pharmacy.pharmacy_id} pharmacy={pharmacy} />
							) : null
						))}
					</div>
					<h1 style={{textAlign: "center"}}>Approved</h1>
					<div className="pharmacy_container">
						{pharmacies && pharmacies.map(pharmacy => (
							pharmacy.is_approved ? (
								<AdminPharmacyCard key={pharmacy.pharmacy_id} pharmacy={pharmacy} />
							) : null
						))}
					</div>
				</Fragment>
			)
			}
		</Fragment>
	)
}

export default AdminPharmacies;
