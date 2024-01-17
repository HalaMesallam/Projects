import React, { Fragment, useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import MetaData from '../Layouts/Metadata/metadata';
import ReactStars from "react-rating-stars-component";

import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { Rating } from '@mui/material';

import './Medicine.css';
import { addReview, clear_errors } from '../../Actions/medicine_actions';
import { addToCartAction } from '../../Actions/cart_action';

import { toast } from 'react-toastify';

const Medicine = ({ medicine, pharmacy }) => {

	const dispatch = useDispatch();
	const { error } = useSelector(state => state.newReview);
	const { isAuthenticated, user, loading } = useSelector(state => state.userLogin);

	const optionsReview = {
		edit: false,
		color: "#d0d0d0",
		activeColor: "#FDCC0D",
		size: window.innerWidth < 992 ? 17 : 20,
		value: medicine[0].medicine_ratings,
		isHalf: true
	}

	const [quantity, setQuantity] = useState(0);

	// Reduce the quantity.
	function reduceQuantity() {
		if (quantity === 0) {
			setQuantity(quantity);
		}
		else {
			const temp = quantity - 1;
			setQuantity(temp);
		}
	}

	// Increase the quantity.
	function addQuantity() {
		if (medicine[0].medicine_stock <= quantity) {
			setQuantity(quantity);
		}
		else {
			const temp = quantity + 1;
			setQuantity(temp);
		}
	}

	const [open, setOpen] = useState(false);
	const theme = useTheme();
	const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

	const [rating, setRating] = useState(0);

		// When submit review button clicked popup opens.
		const handleClickOpen = () => {
			setOpen(true);
		};
	
		// When popup closed.
		const handleClose = () => {
			setOpen(false);
		};

	const submitReview = () => {
		// If User rating is 0, then let them know 0 is not possible.
		if (rating === 0) {
			toast("Rating cannot be zero...");
			return;
		}

		// Otherwise, dispatch an action...
		dispatch(addReview(pharmacy[0].pharmacy_id, medicine[0].medicine_id, rating));
		setOpen(false);

		// Reload the page.
		window.location.reload();
	}

	const addToCart = () => {
		if (!loading) {
			if (!isAuthenticated || !user.is_customer) {
				toast.error("Please login as a customer to add product to cart.");
				return;
			}
			else {
				dispatch(addToCartAction({ medicine: medicine[0], quantity }));
			}
		}
	}

	useEffect(() => {
		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}
	}, [dispatch, error])

	return (
		<Fragment>
			<MetaData title={medicine[0].medicine_name + " - " + pharmacy[0].pharmacy_name} />
			<div className="specific-medicine">

				<div className='medicine-image1'>
					<img
						className="medicine-image"
						src={"/Images/Medicine/" + medicine[0].medicine_image}
						alt={medicine[0].medicine_name}
						key={medicine[0].medicine_id}
					/>
				</div>

				<div className="specific-product-details">
					{/* Name */}
					<div className="product-name">
						<h2>Pharmacy: {pharmacy[0].pharmacy_name}</h2>
						<h2>Medicine: {medicine[0].medicine_name}</h2>
						<p>Medicine # {medicine[0].medicine_id}</p>
					</div>
					{/* Review, Number of Review */}
					<div className="specific-product-reviews">
						<ReactStars {...optionsReview} />
						<span>
							{medicine[0].medicine_total_reviews <= 1 ?
								<span>( {medicine[0].medicine_total_reviews} Review )</span>
								: <span>( {medicine[0].medicine_total_reviews} Reviews )</span>}
						</span>
					</div>
					{/* Product Description */}
					<div className="specific-product-description">
						<p> Description: </p> <p>{medicine[0].medicine_description}</p>
					</div>
					{/* Price and Quantity */}
					<div className="price-quantity">
						<h1>{"$" + medicine[0].medicine_price}</h1>
						<div className="product-add-to-cart">
							{/* Div for Items Count */}
							<div className="specific-product-quantity">
								<button onClick={reduceQuantity}>-</button>
								<input type="text" value={quantity} readOnly />
								<button onClick={addQuantity}>+</button>
							</div>
							{/* Add to Cart Button */}

							<button disabled={medicine[0].medicine_stock <= 0 || quantity <= 0} onClick={addToCart}>Add to Cart</button>
						</div>

						{/* InStock or not with colors and text */}
						<p>
							Status:
							<b className={medicine[0].medicine_stock < 1 ? "red-color" : "green-color"}>
								{medicine[0].medicine_stock < 1 ? " Out of Stock" : " InStock"}
							</b>
						</p>
					</div>

					{/* Submit Review Button */}
					<button className="submit-specific-product-review" onClick={handleClickOpen}>Submit Review</button>
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
						{medicine[0].medicine_name + " Review Submit"}
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
		</Fragment>
	)
}

export default Medicine
