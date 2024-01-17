import React, { Fragment } from 'react';

import './OrderCard.css';

const OrderCard = ({ order }) => {

	const order_qty_total = parseFloat(order.total_price) - parseFloat(order.tax_price)

	return (
		<Fragment>
			<div className='order-container-info'>
				<div className='order-detail-container'>
					<div className='order-information'>
						<div>
							{/* Total of the order */}
							<div className='order-info'>
								<p>TOTAL</p>
								<p>{(order.total_price).toFixed(2)}</p>
							</div>
							<div className='order-info'>
								<p>ITEMS TOTAL</p>
								<p>{(order_qty_total).toFixed(2)}</p>
							</div>
							<div className='order-info'>
								<p>TOTAL TAX</p>
								<p>{(order.tax_price).toFixed(2)}</p>
							</div>
							{/* Ship to name of the person */}
							<div className='order-info'>
								<p>SHIP TO</p>
								<p>{order.customer_name} <br /> {order.order_address}</p>
							</div>
							<div className='order-info'>
								<p>PHARMACY NAME</p>
								<p>{order.pharmacy_name}</p>
							</div>
						</div>
					</div>
					{/* Order Number and link to view details of order */}
					<div className='order-number'>
						<p>ORDER # {order.order_id}</p>
						<p>PHARMACY # {order.pharmacy_id}</p>
						<a href={"/order_details/" + order.order_id}>View Order Details</a>
					</div>
				</div>
			</div>
		</Fragment>
	)
}

export default OrderCard;
