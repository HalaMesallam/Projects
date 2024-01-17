import React, { Fragment } from 'react';
import "./OrderCard.css";

const OrderDetailsCard = ({ medicine }) => {
	return (
		<Fragment>
			<div className='order-product-info'>
				{/* Item Image and link to product */}
				<a href={"/pharmacy/medicines/" + medicine.pharmacy_id + "/" + medicine.medicine_id}>
					<img src={"/Images/Medicine/" + medicine.medicine_image} alt={medicine.medicine_name} />
				</a>
				{/* Item information */}
				<div className='order-item-info'>
					{/* Order item name with link to product */}
					<a href={"/pharmacy/medicines/" + medicine.pharmacy_id + "/" + medicine.medicine_id}>
						<h4>
							{medicine.medicine_name}
						</h4>
					</a>
					{/* Item price and Quantity and But again button with link to product. */}
					<h1>${medicine.medicine_price}</h1>
					<p>QTY: {medicine.quantity}</p>
					<a href={"/pharmacy/medicines/" + medicine.pharmacy_id + "/" + medicine.medicine_id}><button className="buy-product-again">Buy it Again</button></a>
				</div>
			</div>
			{/* Line for design */}
			<hr style={{ marginBottom: "-1px", width: "98%" }} />
		</Fragment>
	)
}

export default OrderDetailsCard
