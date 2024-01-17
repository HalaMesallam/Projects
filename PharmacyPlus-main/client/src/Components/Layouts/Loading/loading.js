import React from 'react'
import MetaData from '../Metadata/metadata';

const Loading = () => {
	return (
		<div className="loading">
			<MetaData title="Loading..." />
			<div className="drawing" id="loading">
				<h1>Loading...</h1>
			</div>
		</div>
	)
}

export default Loading
