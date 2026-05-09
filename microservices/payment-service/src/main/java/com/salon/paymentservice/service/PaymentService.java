package com.salon.paymentservice.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.salon.paymentservice.domain.PaymentMethod;
import com.salon.paymentservice.dto.BookingDTO;
import com.salon.paymentservice.dto.UserDTO;
import com.salon.paymentservice.model.PaymentOrder;
import com.salon.paymentservice.payload.response.PaymentLinkResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod) throws RazorpayException, StripeException;

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorPayPaymentLink(UserDTO user, Long amount, Long orderId) throws RazorpayException;

    String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException;

    //when a user successfully made a payment to update booking status
    //pending to confirmed status
    //change the payment order status also
    Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;

}
