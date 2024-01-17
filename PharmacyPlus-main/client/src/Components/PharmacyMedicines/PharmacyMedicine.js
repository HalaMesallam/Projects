import React, { Fragment, useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router';

import { get_all_medicines, clear_errors } from '../../Actions/medicine_actions';
import { add_pharmacy_review } from '../../Actions/pharmacy_actions';

import { toast } from 'react-toastify';

import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { Rating } from '@mui/material';

import './PharmacyMedicine.css'
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';
import MedicineCard from '../MedicineCard/MedicineCard';

const PharmacyMedicine = () => {

	const { id } = useParams();

	const dispatch = useDispatch();

	const { medicines, error, loading } = useSelector(state => state.allMedicine);
	const { error: pharmacy_review_error } = (state => state.newPharmacyReview);

	useEffect(() => {
		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}

		if (pharmacy_review_error) {
			toast.error(pharmacy_review_error);
			dispatch(clear_errors());
		}

		dispatch(get_all_medicines(id));
	}, [dispatch, error, id]);

	const [open, setOpen] = useState(false);
	const theme = useTheme();
	const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

	const [rating, setRating] = useState(0);

	const submitReview = () => {
		// If User rating is 0, then let them know 0 is not possible.
		if (rating === 0) {
			toast("Rating cannot be zero...");
			return;
		}

		// Otherwise, dispatch an action...
		dispatch(add_pharmacy_review(id, rating));
		setOpen(false);

		// Reload the page.
		window.location.reload();
	}

	// When submit review button clicked popup opens.
	const handleClickOpen = () => {
		setOpen(true);
	};

	// When popup closed.
	const handleClose = () => {
		setOpen(false);
	};

	return (
		<Fragment>
			{loading ? <Loading /> : (
				<Fragment>
					<MetaData title={'All Medicines'} />
					<div style={{textAlign: "center"}}>
						<button className="submit-specific-product-review" onClick={handleClickOpen} >Submit Review</button>
					</div>
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
								{"Submit Review"}
							</DialogTitle>
							<DialogContent>
								{/* rating to set */}
								<div>
									<Rating
										onChange={(e) => setRating(e.target.value)}
										value={rating}
										color="#0F1111"
										activeColor="#FDCC0D"
										size="large"
									/>
								</div>
								{/* TextArea where comment is written */}
								<div className='text-area-and-button'>
									<div>
										{/* Submit Button. */}
										<button id="review-popup-button" onClick={submitReview}>
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
					<div className="medicine_container">
						{medicines && medicines.map(medicine => (
							medicine.is_approved ? (
								<MedicineCard key={medicine.medicine_id} medicine={medicine} />
							) : null
						))}
					</div>
				</Fragment>
			)}
		</Fragment>
	)
}

export default PharmacyMedicine;
