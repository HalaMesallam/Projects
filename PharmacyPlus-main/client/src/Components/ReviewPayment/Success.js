import React, { Fragment, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import MetaData from '../Layouts/Metadata/metadata';
import './Success.css';

const Success = () => {

	const navigate = useNavigate();

	useEffect(() => {
		if (!window.paid) {
			navigate('/payment');
		}
		else {
			window.paid = false;
		}
	}, [navigate]);

	return (
		<Fragment>
			<MetaData title={'Order Placed Success'} />
			<div className='success-container'>
				{/* Container of the card. */}
				<div className="card">
					{/* Circle around check symbol */}
					<div style={{ borderRadius: "200px", height: "200px", width: "200px", backgroundColor: "#F8FAF5", margin: "0 auto" }}>
						{/* CheckMark Symbol */}
						<i className="check-mark">✓</i>
					</div>
					{/* Success Heading */}
					<h1>Success</h1>
					<p>Your order has been placed Successfully...</p>
					{/* Button goes to orders. */}
					<div className='btn-container'>
						<a href="/Orders" className="view-orders-btn"> View Your Order </a>
					</div>
				</div>
			</div>
		</Fragment>
	)
}

export default Success
