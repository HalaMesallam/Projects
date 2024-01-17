import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { toast } from 'react-toastify';
import { clearErrors, pharmacy_details } from '../../Actions/pharmacy_actions';
import Loading from "../Layouts/Loading/loading";
import MetaData from '../Layouts/Metadata/metadata';
import PharmacyDetail from './PharmacyDetail';

const AdminPharmacyDetails = () => {

	const { id } = useParams();
	const dispatch = useDispatch();
	const { loading, error, pharmacy } = useSelector(state => state.pharmacy_details);
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
			clearErrors();
		}

		if (user) {
			dispatch(pharmacy_details(id));
		}
	}, [dispatch, error, isAuthenticated, navigate, user, id, userLoading]);

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					{pharmacy && (
						<Fragment>
							<MetaData title={pharmacy[0].pharmacy_name} />
							<PharmacyDetail pharmacy={pharmacy[0]} />
						</Fragment>
					)}
				</Fragment>
			)}
		</Fragment>
	)
}

export default AdminPharmacyDetails;
