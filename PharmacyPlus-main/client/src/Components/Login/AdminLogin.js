import React, { Fragment, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { clear_errors, admin_login_action } from '../../Actions/login_action';

import { toast } from 'react-toastify';

import './Login.css';
import image from "../../Images/image.jpeg";
import Loading from "../Layouts/Loading/loading";
import MetaData from '../Layouts/Metadata/metadata';

const AdminLogin = () => {

	const dispatch = useDispatch();

	const navigate = useNavigate();
	const { error, isAuthenticated, user, loading } = useSelector(state => state.userLogin);

	useEffect(() => {

		if (error) {
			console.log(error);
			toast.error(error);
			dispatch(clear_errors());
		}

		if (isAuthenticated) {
			if (user.is_admin) {
				navigate("/adminAccount");
			}
			else {
				toast.error('You are not allowed to access this page.');
				navigate("/");
			}
		}
	}, [isAuthenticated, user, error, dispatch, navigate]);

	const [loginEmail, setLoginEmail] = useState("");
	const [loginPassword, setLoginPassword] = useState("");

	// If User click on Login Submit button then dispatch loginUser method in action.
	const loginSubmit = (e) => {
		e.preventDefault();
		dispatch(admin_login_action(loginEmail, loginPassword));
	}

	return (
		<Fragment>
			{loading ? <Loading /> :
				<div className="login-register">
					{/* Give Login title to page. */}
					<MetaData title="Admin Login..." />
					<div className="container">
						<div className="cover">
							{/* Image */}
							<div className="front">
								<img src={image} alt="Login" />
							</div>
						</div>
						<div className="forms">
							<div className="form-content">
								<div className="login-form">
									<div className="title">Login</div>
									<form onSubmit={loginSubmit}>
										<div className="input-boxes">
											{/* Getting Email and Password of User and set it with useState.. */}
											<div className="input-box">
												<i className="fas fa-envelope"></i>
												<input type="email"
													placeholder="Enter your email"
													required
													value={loginEmail}
													onChange={(e) => setLoginEmail(e.target.value)} />
											</div>
											<div className="input-box">
												<i className="fas fa-lock"></i>
												<input type="password"
													placeholder="Enter your password"
													required
													value={loginPassword}
													onChange={(e) => setLoginPassword(e.target.value)} />
											</div>
											<div className="button input-box">
												<input type="submit" value="Log In" />
											</div>
											{/* Switch to sign up page. */}
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			}
		</Fragment>
	)
}

export default AdminLogin;
