// export const URL = "https://easthomes-develop.herokuapp.com";
 export const URL = 'http://localhost:8080';
export const GET_SERVICE = URL + '/service/services';
export const POST_SERVICE = URL + '/service/services';
export const POST_SERVICE_REVIEW = URL + '/serviceReview/add';
export const FILTER_SERVICE = URL + '/service/services/filter';
export const PUT_SERVICE = URL + '/service'
export const DELETE_SERVICE = URL + '/service/services'

export const GET_PROPERTY = URL + '/property/properties';
export const POST_PROPERTY = URL + '/property/property';
export const PUT_PROPERTY = URL + '/property';
export const DELETE_PROPERTY = URL + '/property/properties';
export const FILTER_PROPERTY = URL + '/property/properties/filter';
export const POST_PAYMENT = URL + '/payment/addPayment';
export const AUTH_USER = URL + '/user/authenticate';
export const REGISTER_USER = URL + "/user/register";
export const SERVICE_BOOK_APPOINTMENT = URL + '/service/contact';
export const PROPERTY_SCHEDULE_MEETING = URL + '/property/owner/contact';
export const REST_USER = URL + "/rest/user/register";
export const PAYMENT_CONFIRMATION_RECEIPT = URL + "/paymentreceipt/confirmation";
export const FAVORITE_PROPERTY = URL + '/favorite-property/';
export const FAVORITE_SERVICE = URL + '/favorite-service/';
export const FORGOT_PASSWORD = URL + '/user/forgotpassword';
export const OTP_VERIFICATION = URL + "/user/otpverification";
export const NEW_PASSWORD = URL + "/user/newpassword";

export const SOCKET_URL = URL + '/ws';
