import React, { Fragment, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import './Account.css';

const CustomerAccount = () => {

	const navigate = useNavigate();
	const { user, loading, isAuthenticated } = useSelector(state => state.userLogin);

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated) {
				navigate("/");
			} else {
				if (!user.is_customer) {
					toast("You cannot access Customer Account.")
					navigate("/");
				}
			}
		}
	}, [isAuthenticated, loading, navigate, user]);

	return (
		<Fragment>
			{loading ? <Loading /> :
				<Fragment>
					<MetaData title="Your Account" />
					{user && (
					<div className="user-information">
						<div className="user-information-container">
							<div className="info">
								<div>
									{/* User Full name. */}
									<h4>Full Name:</h4>
									<p>{user.customer[0].customer_name}</p>
								</div>
							</div>
							<div className="info">
								<div>
									{/* User email. */}
									<h4>Email:</h4>
									<p>{user.customer[0].customer_email}</p>
								</div>
							</div>
							<div className="info">
								<div>
									{/* User email. */}
									<h4>Address:</h4>
									<p>{user.customer[0].customer_address}</p>
								</div>
							</div>
							<div className="info">
								<div>
									{/* User email. */}
									<h4>Phone:</h4>
									<p>{user.customer[0].customer_phone}</p>
								</div>
							</div>
							<div>
								{/* Password. */}
								<h4>Password:</h4>
								<strong>**********</strong>
							</div>
							<div>
								{/* User orders and changing password buttons. */}
								<div>
									<a href="/Orders" className='other-button'>Orders</a> <br /> <br /> <br />
								</div>
								<div>
									<a href="/Cart" className="other-button">Cart</a>
								</div>
							</div>
						</div>
					</div>
					)}
				</Fragment>
			}
		</Fragment>
	)
}

export default CustomerAccount;
