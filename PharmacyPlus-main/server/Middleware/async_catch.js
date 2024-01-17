module.exports = error_handler => (req, res, next) => {
	Promise.resolve(error_handler(req, res, next)).catch(next);
};
