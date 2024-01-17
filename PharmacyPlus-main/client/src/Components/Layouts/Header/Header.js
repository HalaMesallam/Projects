import React, { Fragment } from 'react';
import { useSelector } from 'react-redux';

import logo from '../../../Images/logo.png';
import './Header.css';

const Header = () => {

	const { isAuthenticated, loading, user } = useSelector(state => state.userLogin);

	function logout() {
		localStorage.removeItem('loginInfo');
		window.location.href = '/';
	}

	return (
		<Fragment>
			<section className="navBar">
				{/* NavBar Logo and Search Items */}
				<nav className="navbar navbar-expand-lg navbar-dark">
					<a className="navbar-brand" href="/"><img className="logo-nav" src={logo} alt="Logo" /></a>
					<button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
						<span className="navbar-toggler-icon"></span>
					</button>

					{/* NavBar three links */}
					<div className="collapse navbar-collapse" id="navbarSupportedContent">
						{/* <form className="navbar-brand search-nav" onSubmit={searchSubmitHandler}>
							<input
								className="search-field-nav" type="text" placeholder="Search..." autoComplete='off'
								name="search"
								onChange={(event) => setSearchWords(event.target.value)} />
							<button className="btn btn-warning search-btn-nav" type="submit"><i className="fa fa-search"></i></button>
						</form> */}
						<ul className="navbar-nav ml-auto">
							{!loading && isAuthenticated && user.is_admin === true ? (
								<Fragment>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/adminAccount"> <span>Account </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/adminPharmacies"> <span>Pharmacies </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/adminMedicines"> <span>Medicines </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/" onClick={logout}><span>Logout </span></a>
									</li>
								</Fragment>
							) : !loading && isAuthenticated && user.is_customer === true ? (
								<Fragment>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/customerAccount"> <span>Account </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/Orders"> <span>Orders </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/Cart"> <span>Cart </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/" onClick={logout}><span>Logout </span></a>
									</li>
								</Fragment>
							) : !loading && isAuthenticated && user.is_owner === true ? (
								<Fragment>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/ownerAccount"> <span><span>Account </span></span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/requestPharmacy"> <span> <span>Request </span>  <br /> <span> Pharmacy </span> </span></a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/requestMedicine"> <span> <span>Request </span>  <br /> <span> Medicine </span> </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/pharmacyOwnerOrders"> <span> <span>Your Pharmacy </span>  <br /> <span> Orders </span> </span> </a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/" onClick={logout}><span><span>Logout </span></span></a>
									</li>
								</Fragment>
							) : (
								<Fragment>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/customerLogin"><span> <span>Customer </span>  <br /> <span> Sign-In & Register </span> </span></a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/pharmacyOwnerLogin"><span> <span>Pharmacy Owner </span>  <br /> <span> Sign-In & Register </span> </span></a>
									</li>
									<li className="nav-item sign-nav">
										<a className="nav-link sign-in-nav" href="/adminLogin"><span> <span>Admin </span>  <br /> <span> Sign-In </span> </span></a>
									</li>
									<li className="nav-item">
										<a className="nav-link order-nav" href="/Orders"><span className='order-nav'>  Orders</span></a>
									</li>
									<li className="nav-item">
										<a className="nav-link cart-nav" href="/cart"><span className='cart-nav'>  Cart </span></a>
									</li>
								</Fragment>
							)}
						</ul>
					</div>
				</nav >
			</section>
		</Fragment>
	)
}

export default Header
