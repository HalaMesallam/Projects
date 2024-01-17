import React, { Fragment, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import { toast } from 'react-toastify';

import './SpecificMedicine.css';
import { medicine_details, clear_errors } from '../../Actions/medicine_actions';
import { pharmacy_details, clearErrors } from '../../Actions/pharmacy_actions';

import Loading from '../Layouts/Loading/loading';
import Medicine from '../Medicine/Medicine';

const SpecificMedicine = () => {

	const dispatch = useDispatch();

	const { medicine_id } = useParams();
	const { pharmacy_id } = useParams();

	const { loading, error, medicine } = useSelector(state => state.medicine_details);
	const { loading: pharmacy_loading, error: pharmacy_error, pharmacy } = useSelector(state => state.pharmacy_details)

	useEffect(() => {
		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}

		if (pharmacy_error) {
			toast.error(pharmacy_error);
			dispatch(clearErrors());
		}

		dispatch(medicine_details(pharmacy_id, medicine_id));
		dispatch(pharmacy_details(pharmacy_id));

	}, [dispatch, error, pharmacy_error, medicine_id, pharmacy_id]);

	return (
		<Fragment>
			{loading === true || pharmacy_loading ? <Loading /> : (
				<Fragment>
					{medicine && pharmacy && (
						<Medicine medicine={medicine} pharmacy={pharmacy} />
					)}
				</Fragment>
			)}
		</Fragment>
	)
}

export default SpecificMedicine;
