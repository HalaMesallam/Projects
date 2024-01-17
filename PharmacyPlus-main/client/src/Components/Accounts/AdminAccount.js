import React, { Fragment, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import Loading from '../Layouts/Loading/loading';
import MetaData from '../Layouts/Metadata/metadata';

import './Account.css';

const AdminAccount = () => {

	const navigate = useNavigate();
	const { user, loading, isAuthenticated } = useSelector(state => state.userLogin);

	useEffect(() => {
		if (!loading) {
			if (!isAuthenticated) {
				navigate("/");
			} else {
				if (!user.is_admin) {
					toast("You cannot access Admin Account.")
					navigate("/");
				}
			}
		}
	}, [isAuthenticated, loading, navigate, user]);

	return (
		<Fragment>
			{loading ? <Loading /> :
				<Fragment>
					<MetaData title="Admin Account" />
					{user && (
					<div className="user-information">
						<div className="user-information-container">
							<div className="info">
								<div>
									{/* User Full name. */}
									<h4>Full Name:</h4>
									<p>{user.admin[0].admin_name}</p>
								</div>
							</div>
							<div className="info">
								<div>
									{/* User email. */}
									<h4>Email:</h4>
									<p>{user.admin[0].admin_email}</p>
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
									<a href="/adminPharmacies" className='other-button'>Requested Pharmacies</a> <br /> <br /> <br />
								</div>
								<div>
									<a href="/adminMedicines" className="other-button">Requested Medicines</a>
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

export default AdminAccount
