import React, { Fragment } from 'react';

import MetaData from '../Layouts/Metadata/metadata';

// Not Found Component.
const NotFound = () => {
	return (
		<Fragment>
			{/* Title of the page. */}
			<MetaData title="Page Not Found...404" />
			<div style={{textAlign: "center"}}>
			<h1>Error 404</h1>
			<br />
			<br />
			{/* Button to home page. */}
			<a href="/"><button className='submit-specific-product-review'>HOME</button></a>
			</div>
		</Fragment>
	)
}

export default NotFound
