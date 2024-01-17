import React, { Fragment, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import ReactStars from "react-rating-stars-component";
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { DialogContentText, MenuItem, Select } from '@mui/material';

import { toast } from 'react-toastify';
import MetaData from '../Layouts/Metadata/metadata';
import { change_medicine_approval } from '../../Actions/admin_action';

const MedicineDetail = ({ medicine, pharmacy }) => {

	const optionsReview = {
		edit: false,
		color: "#d0d0d0",
		activeColor: "#FDCC0D",
		size: window.innerWidth < 992 ? 17 : 20,
		value: medicine.medicine_ratings,
		isHalf: true
	}

	const navigate = useNavigate();
	const dispatch = useDispatch();
	const [open, setOpen] = useState(false);
	const theme = useTheme();
	const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

	const [approve, setApprove] = useState(medicine.is_approved ? 'approved' : 'not approved');

	// When submit review button clicked popup opens.
	const handleClickOpen = () => {
		setOpen(true);
	};

	// When popup closed.
	const handleClose = () => {
		setOpen(false);
	};

	const submitDecision = async () => {

		const is_medicine_approved = medicine.is_approved === 1 ? 'approved' : 'not approved';

		if (approve === is_medicine_approved) {
			toast("Change your decision...");
			return;
		}

		// Otherwise, dispatch an action...
		dispatch(change_medicine_approval(medicine.medicine_id, approve));
		setOpen(false);

		// Reload the page.
		navigate("/adminMedicines")
	}

	return (
		<Fragment>
			<MetaData title={pharmacy.pharmacy_name} />
			<div className="specific-medicine">

				<div className='medicine-image1'>
					<img
						className="medicine-image"
						src={"/Images/Medicine/" + medicine.medicine_image}
						alt={medicine.medicine_name}
						key={medicine.medicine_id}
					/>
				</div>

				<div className="specific-product-details">
					{/* Name */}
					<div className="product-name">
						<h2>{medicine.medicine_name}</h2>
						<h4>Stock: {medicine.medicine_stock}</h4>
						<p>Medicine # {medicine.medicine_id}</p>
					</div>
					<div className="product-name">
						<h2>{pharmacy.pharmacy_name}</h2>
						<h4>Pharmacy Address: {pharmacy.pharmacy_address}</h4>
						<p>Pharmacy # {pharmacy.pharmacy_id}</p>
					</div>
					{/* Review, Number of Review */}
					<div className="specific-product-reviews">
						<ReactStars {...optionsReview} />
						<span>
							{medicine.medicine_total_reviews === 0 || 1 ?
								<span>( {medicine.medicine_total_reviews} Review )</span>
								: <span>( {medicine.medicine_total_reviews} Reviews )</span>}
						</span>
					</div>
					{/* Product Description */}
					<div className="specific-product-description">
						<p> Description: </p> <p>{medicine.medicine_description}</p>
					</div>

					Status:
					<b className={medicine.is_approved === 0 ? "red-color" : "green-color"}>
						{medicine.is_approved === 0 ? " Not Approved" : " Approved"}
					</b>
					<button className="submit-specific-product-review" onClick={handleClickOpen}>Change Approval</button>
				</div>
			</div>
			{/* Review popup */}
			<div className='popup-review'>
				{/* When Dialog box opens when close. */}
				<Dialog
					fullScreen={fullScreen}
					open={open}
					onClose={handleClose}
					aria-labelledby="responsive-dialog-title"
				>
					{/* Title of the popup */}
					<DialogTitle id="responsive-dialog-title" style={{ fontFamily: "Comic Neue, cursive", fontSize: "2.5rem" }} >
						{medicine.medicine_name + " Approve?"}
					</DialogTitle>
					<DialogContent>
						<DialogContentText>
							<Select
								onChange={(e) => setApprove(e.target.value)}
								style={{ width: "100%", height: "50px", fontSize: "1.5rem" }}
							>
								<MenuItem value="approved">Approve</MenuItem>
								<MenuItem value="not approved">Not Approve</MenuItem>
							</Select>
						</DialogContentText>
						{/* TextArea where comment is written */}
						<div className='text-area-and-button'>
							<div>
								{/* Submit Button. */}
								<button id="review-popup-button" onClick={submitDecision}>
									Submit
								</button>
								{/* Cancel Button */}
								<button id="review-popup-button" onClick={handleClose}>
									Cancel
								</button>
							</div>
						</div>
					</DialogContent>
				</Dialog>
			</div>
		</Fragment>
	)
}

export default MedicineDetail;
