import React, { Fragment } from 'react';

import './MedicineCard.css';
import ReactStars from 'react-rating-stars-component';

const MedicineCard = ({medicine}) => {

	const options = {
		edit: false,
		color: "#d0d0d0",
		activeColor: "#FDCC0D",
		size: window.innerWidth < 900 ? 13 : 20,
		value: medicine.medicine_ratings,
		isHalf: true
	}

	return (
		<Fragment>
			<a className='medicineCard' href={"/pharmacy/medicines/" + medicine.pharmacy_id + "/" + medicine.medicine_id}>
				<img src={'/Images/Medicine/' + medicine.medicine_image} alt={medicine.medicine_name} />
				<p>{medicine.medicine_name}</p>
				{medicine.medicine_description.length < 50 ? 
				(<span>{medicine.medicine_description}</span>
				) : (<span>{medicine.medicine_description.substring(0, 50) + "..."}</span>)}
				<div>
					<ReactStars {...options} />
					<span>
						({medicine.medicine_total_reviews > 1 ? medicine.medicine_total_reviews + " Reviews" : medicine.medicine_total_reviews + " Review"})
					</span>
				</div>

				<span className='price'>${medicine.medicine_price}</span>
			</a>
		</Fragment>
	)
}

export default MedicineCard
