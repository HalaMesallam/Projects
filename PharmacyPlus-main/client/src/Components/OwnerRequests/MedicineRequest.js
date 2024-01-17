import React, { Fragment, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import AbcIcon from '@mui/icons-material/Abc';
import SellIcon from '@mui/icons-material/Sell';
import ImageIcon from '@mui/icons-material/Image';
import StorageIcon from '@mui/icons-material/Storage';
import DateRangeIcon from '@mui/icons-material/DateRange';
import DescriptionIcon from '@mui/icons-material/Description';

import { toast } from 'react-toastify';
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import "./requests.css";
import { requestMedicine, clear_errors } from '../../Actions/owner_action';

const MedicineRequest = () => {

	const navigate = useNavigate();
	const dispatch = useDispatch();

	const { user, loading, isAuthenticated, error } = useSelector(state => state.userLogin);
	const { success, error: request_error } = useSelector(state => state.request_medicine);

	const [pharmacyId, setPharmacyId] = useState();
	const [medicineName, setMedicineName] = useState();
	const [medicineDescription, setMedicineDescription] = useState();
	const [medicinePrice, setMedicinePrice] = useState();
	const [medicineImage, setMedicineImage] = useState("");
	const [medicineStock, setMedicineStock] = useState();
	const [medicineExpiryDate, setMedicineExpiryDate] = useState();

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
			toast.success('Medicine Requested Successfully');
			dispatch({ type: 'RESET_PHARMACY_REQUEST_SUCCESS' });
			navigate('/ownerAccount');
		}
	}, [isAuthenticated, navigate, user, error, dispatch, success, request_error]);

	const addImages = (e) => {
		setMedicineImage(e.target.files[0]);

		const reader = new FileReader();

		reader.onload = () => {
			if (reader.readyState === 2) {
				setMedicineImage(reader.result);
			}
		}

		reader.readAsDataURL(e.target.files[0]);
	}

	const newMedicineRequest = (e) => {
		e.preventDefault();

		const owner_id = user.owner[0].owner_id;

		const medicineData = {
			pharmacy_id: pharmacyId,
			medicine_name: medicineName,
			medicine_description: medicineDescription,
			medicine_price: medicinePrice,
			medicine_image: medicineImage,
			medicine_stock: medicineStock,
			medicine_expiry_date: medicineExpiryDate,
			owner_id
		}

		dispatch(requestMedicine(medicineData));
	}

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'Medicine Owner Requests'} />
					<div className='new-product-container'>
						{/* Form of creating a product */}
						<form className='product-create-form' onSubmit={newMedicineRequest}>
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
								<h5> Medicine Name: </h5>
								<AbcIcon />
								<input
									type="text"
									placeholder='Medicine Name...'
									required
									value={medicineName}
									onChange={(e) => setMedicineName(e.target.value)} />
							</div>
							<div>
								<h5> Medicine Stock: </h5>
								<StorageIcon />
								<input
									type="number"
									placeholder='Medicine Stock...'
									required
									value={medicineStock}
									onChange={(e) => setMedicineStock(e.target.value)} />
							</div>
							<div>
								<h5> Medicine Price: </h5>
								<SellIcon />
								<input
									type="number"
									placeholder='Medicine Price...'
									required
									value={medicinePrice}
									onChange={(e) => setMedicinePrice(e.target.value)} />
							</div>
							<div>
								<h5> Medicine Expiry Date: </h5>
								<DateRangeIcon />
								<input
									type="date"
									placeholder='Medicine Expiry Date...'
									required
									value={medicineExpiryDate}
									onChange={(e) => setMedicineExpiryDate(e.target.value)} />
							</div>
							<div>
								<h5> Medicine Description: </h5>
								<DescriptionIcon />
								<textarea
									placeholder='Medicine Description...'
									required
									value={medicineDescription}
									onChange={(e) => setMedicineDescription(e.target.value)}
									rows="2"
									cols="30" />
							</div>
							<div className='product-image'>
								<h5> Medicine Image: </h5>
								<ImageIcon />
								<input
									type="file"
									accept='image/*'
									name='Medicine Image'
									onChange={addImages}
									disabled={medicineImage !== "" ? true : false}
									required />
							</div>
							<button type="submit" className='create-product-button' >
								Request Medicine
							</button>
						</form>
					</div>
				</Fragment>)}
		</Fragment>
	)
}

export default MedicineRequest
