import React, { Fragment, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux';

import { get_all_pharmacies, clearErrors } from '../../Actions/pharmacy_actions';

import { toast } from 'react-toastify';

import './Home.css';
import Loading from '../Layouts/Loading/loading';
import PharmacyCard from '../PharmacyCard/PharmacyCard';
import MetaData from '../Layouts/Metadata/metadata';

const Home = () => {

	const dispatch = useDispatch();

	const { pharmacies, error, loading } = useSelector(state => state.allPharmacies);

	useEffect(() => {
		if (error) {
			toast.error(error);
			dispatch(clearErrors());
		}

		dispatch(get_all_pharmacies());

	}, [dispatch, error]);

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'Home - All Pharmacies'} />
					<div className="pharmacy_container">
						{pharmacies && pharmacies.map(pharmacy => (
							pharmacy.is_approved ? (
								<PharmacyCard key={pharmacy.pharmacy_id} pharmacy={pharmacy} />
							) : null
						))}
					</div>
				</Fragment>
			)
			}
		</Fragment >
	)
}

export default Home;
