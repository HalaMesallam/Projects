import React, { Fragment } from 'react'

import ReactStars from 'react-rating-stars-component';

import "../PharmacyCard/PharmacyCard.css";

const AdminPharmacyCard = ({ pharmacy }) => {

	const options = {
		edit: false,
		color: "#d0d0d0",
		activeColor: "#FDCC0D",
		size: window.innerWidth < 900 ? 13 : 20,
		value: pharmacy.pharmacy_ratings,
		isHalf: true
	}

	return (
		<Fragment>
			<a className='pharmacyCard' href={"/adminPharmacyDetails/" + pharmacy.pharmacy_id}>
				<img src={'/Images/Pharmacy/' + pharmacy.pharmacy_image_path} alt={pharmacy.pharmacy_name} />
				<p>{pharmacy.pharmacy_name}</p>
				<span>{pharmacy.pharmacy_address}</span>

				<div>
					<ReactStars {...options} />
					<span>
						({pharmacy.pharmacy_total_reviews > 1 ? pharmacy.pharmacy_total_reviews + " Reviews" : pharmacy.pharmacy_total_reviews + " Review"})
					</span>
				</div>
			</a>
		</Fragment>
	)
}

export default AdminPharmacyCard
