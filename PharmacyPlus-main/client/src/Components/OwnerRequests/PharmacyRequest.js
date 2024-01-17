import React, { Fragment, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import AbcIcon from '@mui/icons-material/Abc';
import SellIcon from '@mui/icons-material/Sell';
import ImageIcon from '@mui/icons-material/Image';

import { toast } from 'react-toastify';
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import "./requests.css";
import { requestPharmacy, clear_errors } from '../../Actions/owner_action';

const PharmacyRequest = () => {

	const navigate = useNavigate();
	const dispatch = useDispatch();
	const { user, loading, isAuthenticated, error } = useSelector(state => state.userLogin);
	const { success, error: request_error } = useSelector(state => state.request_pharmacy);

	const [pharmacyId, setPharmacyId] = useState();
	const [pharmacyName, setPharmacyName] = useState();
	const [pharmacyAddress, setPharmacyAddress] = useState();
	const [pharmacyImage, setPharmacyImage] = useState("");


	useEffect(() => {
		if (!isAuthenticated) {
			navigate('/pharmacyOwnerLogin');
		}
		else {
			if (!user.is_owner) {
				toast.error('You are not a pharmacy owner');
				navigate('/');
			}
		}
		if (error) {
			toast.error(error);
		}

		if (request_error) {
			toast.error(request_error);
			dispatch(clear_errors());
		}

		if (success) {
			toast.success('Pharmacy request sent successfully');
			dispatch({ type: 'RESET_PHARMACY_REQUEST_SUCCESS' });
			navigate('/ownerAccount');
		}

	}, [isAuthenticated, navigate, user, error, dispatch, success, request_error]);

	const createPharmacySubmit = (e) => {
		e.preventDefault();

		const owner_id = user.owner[0].owner_id;

		const pharmacy = {
			pharmacy_id: pharmacyId,
			pharmacy_name: pharmacyName,
			pharmacy_address: pharmacyAddress,
			pharmacy_image: pharmacyImage,
			owner_id: owner_id
		}

		dispatch(requestPharmacy(pharmacy));
	}

	const addImages = (e) => {
		setPharmacyImage(e.target.files[0]);

		const reader = new FileReader();

		reader.onload = () => {
			if (reader.readyState === 2) {
				setPharmacyImage(reader.result);
			}
		}

		reader.readAsDataURL(e.target.files[0]);
	}

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'Pharmacy Owner Requests'} />
					<div className='new-product-container'>
						{/* Form of creating a product */}
						<form className='product-create-form' onSubmit={createPharmacySubmit}>
							{/* Product Name Div */}
							<div>
							<h5> Pharmacy ID: </h5>
								<AbcIcon />
								<input
									type="text"
									placeholder='Pharmacy ID...'
									required
									value={pharmacyId}
									onChange={(e) => setPharmacyId(e.target.value)} />
								<h5> Pharmacy Name: </h5>
								<AbcIcon />
								<input
									type="text"
									placeholder='Pharmacy Name...'
									required
									value={pharmacyName}
									onChange={(e) => setPharmacyName(e.target.value)} />
							</div>
							{/* Product Price Div */}
							<div>
								<h5> Pharmacy Address: </h5>
								<SellIcon />
								<input
									type="text"
									placeholder=' Pharmacy Address...'
									required
									value={pharmacyAddress}
									onChange={(e) => setPharmacyAddress(e.target.value)} />
							</div>
							<div className='product-image'>
								<h5> Pharmacy Image: </h5>
								<ImageIcon />
								<input
									type="file"
									accept='image/*'
									name='Product Image'
									onChange={addImages}
									disabled={pharmacyImage !== "" ? true : false}
									required />
							</div>
							<button type="submit" className='create-product-button' >
								Request Pharmacy
							</button>
						</form>
					</div>
				</Fragment>)}
		</Fragment>
	)
}

export default PharmacyRequest;
