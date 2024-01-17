import React, { Fragment, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { clear_errors, customer_login_action, customer_register_action } from '../../Actions/login_action';

import { toast } from 'react-toastify';

import './Login.css';
import image from "../../Images/image.jpeg";
import Loading from "../Layouts/Loading/loading";
import MetaData from '../Layouts/Metadata/metadata';

export const CustomerLogin = () => {
	const dispatch = useDispatch();

	const navigate = useNavigate();
	const { error, isAuthenticated, user, loading } = useSelector(state => state.userLogin);

	useEffect(() => {

		if (error) {
			toast.error(error);
			dispatch(clear_errors());
		}

		if (isAuthenticated) {
			if (user.is_customer) {
				navigate("/customerAccount");
			}
			else {
				toast.error('You are not allowed to access this page.');
				navigate("/");
			}
		}
	}, [isAuthenticated, user, error, dispatch, navigate]);

	const [loginEmail, setLoginEmail] = useState("");
	const [loginPassword, setLoginPassword] = useState("");

	const [registerName, setRegisterName] = useState("");
	const [registerEmail, setRegisterEmail] = useState("");
	const [registerPassword, setRegisterPassword] = useState("");
	const [registerAddress, setRegisterAddress] = useState("");
	const [registerPhone, setRegisterPhone] = useState("");

	// If User click on Login Submit button then dispatch loginUser method in action.
	const loginSubmit = (e) => {
		e.preventDefault();
		dispatch(customer_login_action(loginEmail, loginPassword));
	}

	const registerSubmit = (e) => {
		e.preventDefault();
		dispatch(customer_register_action(registerName, registerEmail, registerPassword, registerAddress, registerPhone));
	}

	return (
		<Fragment>
			{loading ? <Loading /> :
				<div className="login-register">
					{/* Give Login title to page. */}
					<MetaData title="Customer Login..." />
					<div className="container">
						<input type="checkbox" id="flip" />
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
											<div className="text sign-up-text">Don't have an account? <label htmlFor="flip">SignUp now</label></div>
										</div>
									</form>
								</div>
								<div className="signup-form">
									<div className="title">Signup</div>
									<form onSubmit={registerSubmit}>
										{/* Get User name, email and password and set it with useState. */}
										<div className="input-boxes">
											<div className="input-box">
												<i className="fas fa-user"></i>
												<input type="text"
													placeholder="Enter your name"
													required
													name='name'
													value={registerName}
													onChange={(e) => setRegisterName(e.target.value)} />
											</div>
											<div className="input-box">
												<i className="fas fa-envelope"></i>
												<input type="tel"
													placeholder="Enter your phone"
													required
													name='phone'
													value={registerPhone}
													onChange={(e) => setRegisterPhone(e.target.value)} />
											</div>
											<div className="input-box">
												<i className="fas fa-envelope"></i>
												<input type="text"
													placeholder="Enter your address"
													required
													name='phone'
													value={registerAddress}
													onChange={(e) => setRegisterAddress(e.target.value)} />
											</div>
											<div className="input-box">
												<i className="fas fa-envelope"></i>
												<input type="email"
													placeholder="Enter your email"
													required
													name='email'
													value={registerEmail}
													onChange={(e) => setRegisterEmail(e.target.value)} />
											</div>
											<div className="input-box">
												<i className="fas fa-lock"></i>
												<input type="password"
													placeholder="Enter your password"
													required
													name='password'
													value={registerPassword}
													onChange={(e) => setRegisterPassword(e.target.value)}
												/>
											</div>
											<div className="button input-box">
												<input type="submit"
													value="Register" />
											</div>
											<div className="text sign-up-text">Already have an account? <label htmlFor="flip">Login now</label></div>
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

export default CustomerLogin;
