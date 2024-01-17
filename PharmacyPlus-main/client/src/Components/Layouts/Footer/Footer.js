import React from 'react';
import './Footer.css';

// Importing Images of Footer.
import IOSApp from '../../../Images/appStoreDownload.png';
import androidApp from '../../../Images/playStoreDownload.png';
import logo from '../../../Images/logo.png';


const Footer = () => {
	return (
		<section id="foo">
			<div className="row">
				{/* First Part */}
				<div className="col-lg-4 col-md-6">
					<div className="footer">
						<h3>Download Our App for IOS and Android</h3>
						{/* eslint-disable-next-line */}
						<a href="https://www.apple.com/ca/app-store/" target="_blank"><img className="ios" src={IOSApp} alt="Download IOS App" /></a>
						{/* eslint-disable-next-line */}
						<a href="https://play.google.com/store" target="_blank"><img className="android" src={androidApp} alt="Download Android App" /></a>
					</div>
				</div>
				{/* Second Part */}
				<div className="col-lg-4 col-md-6">
					<div className="footer">
						<a href="/"><img className="logo-footer" src={logo} alt="Web-site logo" /></a>
						<p>Copyright 2022 &copy; PharmacyPlus</p>
					</div>
				</div>
				{/* Third Part */}
				<div className="col-lg-4 col-md-12">
					<div className="footer">
						<h3 className='footer-contact'>CONTACT US</h3>
						{/* eslint-disable-next-line */}
						<a href="/" target="_blank"><i className="fab fa-youtube fa-lg icon-footer"></i></a>
						{/* eslint-disable-next-line */}
						<a href="/" target="_blank"><i className="fab fa-instagram fa-lg icon-footer"></i></a>
						{/* eslint-disable-next-line */}
						<a href="/" target="_blank"><i className="fab fa-twitter fa-lg icon-footer"></i></a>
						{/* eslint-disable-next-line */}
						<a href="/" target="_blank"><i className="fab fa-linkedin fa-lg icon-footer"></i></a>
					</div>
				</div>
			</div>

		</section>
	)
}

export default Footer;
