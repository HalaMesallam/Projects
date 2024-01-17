import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { toast } from 'react-toastify';

import '../Home/Home.css';
import { get_all_medicines_admin, clear_errors } from "../../Actions/admin_action";
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';
import AdminMedicineCard from './AdminMedicineCard.js';

const AdminMedicines = () => {

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const { medicines, error, loading } = useSelector(state => state.admin_medicines);
	const { loading: userLoading, isAuthenticated, user } = useSelector(state => state.userLogin);

	useEffect(() => {
		if (!userLoading) {
			if (!isAuthenticated || !user || !user.is_admin ) {
				navigate('/adminLogin');
			}
		}

		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}

		if (user) {
			dispatch(get_all_medicines_admin());
		}

	}, [dispatch, error, isAuthenticated, navigate, user, userLoading]);

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'Admin - All Medicines'} />
					<h1 style={{ textAlign: "center" }}>Not Approved</h1>
					<div className="pharmacy_container">
						{medicines && medicines.map(medicine => (
							medicine.is_approved === 0 ? (
								<AdminMedicineCard key={medicine.medicine_id} medicine={medicine} />
							) : null
						))}
					</div>
					<h1 style={{ textAlign: "center" }}>Approved</h1>
					<div className="pharmacy_container">
						{medicines && medicines.map(medicine => (
							medicine.is_approved === 1 ? (
								<AdminMedicineCard key={medicine.medicine_id} medicine={medicine} />
							) : null
						))}
					</div>
				</Fragment>
			)
			}
		</Fragment>
	)
}

export default AdminMedicines;
