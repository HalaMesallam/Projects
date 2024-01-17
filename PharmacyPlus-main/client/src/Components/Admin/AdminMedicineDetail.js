import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { toast } from 'react-toastify';
import { clear_errors, medicine_details } from '../../Actions/medicine_actions';
import { pharmacy_details, clearErrors } from "../../Actions/pharmacy_actions";
import Loading from "../Layouts/Loading/loading";
import MetaData from '../Layouts/Metadata/metadata';
import MedicineDetail from './MedicineDetail.js';

const AdminMedicineDetail = () => {

	const { pharmacy_id, medicine_id } = useParams();
	const dispatch = useDispatch();
	const { loading, error, medicine } = useSelector(state => state.medicine_details);
	const { loading: pharmacyLoading, error: pharmacyError, pharmacy } = useSelector(state => state.pharmacy_details);
	const { loading: userLoading, isAuthenticated, user } = useSelector(state => state.userLogin);
	const navigate = useNavigate();

	useEffect(() => {
		if (!userLoading) {
			if (!isAuthenticated || !user || !user.is_admin) {
				navigate('/adminLogin');
			}
		}

		if (error) {
			toast.error(error);
			clear_errors();
		}

		if (pharmacyError) {
			toast.error(pharmacyError);
			clearErrors();
		}

		if (user) {
			dispatch(pharmacy_details(pharmacy_id));
			dispatch(medicine_details(pharmacy_id, medicine_id));
		}
	}, [dispatch, error, isAuthenticated, navigate, user, pharmacy_id, medicine_id, userLoading, pharmacyError]);

	return (
		<Fragment>
			{loading || pharmacyLoading ? <Loading /> : (
				<Fragment>
					{medicine && (
						<Fragment>
							<MetaData title={medicine[0].medicine_name} />
							<MedicineDetail medicine={medicine[0]} pharmacy={pharmacy[0]} />
						</Fragment>
					)}
				</Fragment>
			)}
		</Fragment>
	)
}

export default AdminMedicineDetail;
