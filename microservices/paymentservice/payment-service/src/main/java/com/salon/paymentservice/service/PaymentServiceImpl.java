package com.salon.paymentservice.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.salon.paymentservice.domain.PaymentMethod;
import com.salon.paymentservice.domain.PaymentOrderStatus;
import com.salon.paymentservice.dto.BookingDTO;
import com.salon.paymentservice.dto.UserDTO;
import com.salon.paymentservice.model.PaymentOrder;
import com.salon.paymentservice.payload.response.PaymentLinkResponse;
import com.salon.paymentservice.repository.PaymentOrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${razorpay.api.key}")
    private String razorPayApiKey;

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    @Value("${razorpay.api.secret}")
    private String razorPaySecretKey;

    @Override
    public PaymentLinkResponse createOrder(UserDTO user, 
                                           BookingDTO booking, 
                                           PaymentMethod paymentMethod) throws RazorpayException, StripeException {
        Long totalPrice = (long) booking.getTotalPrice();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setAmount(totalPrice);
        paymentOrder.setBookingId(booking.getId());
        paymentOrder.setUserId(user.getId());
        paymentOrder.setSalonId(booking.getSalonId());

        PaymentOrder savedOrder = paymentOrderRepository.save(paymentOrder);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        //create payment link
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink paymentLink = createRazorPayPaymentLink(user, savedOrder.getAmount(), savedOrder.getId());

            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");
            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);

            savedOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(savedOrder);
        } else {
            String paymentUrl = createStripePaymentLink(user, savedOrder.getAmount(), savedOrder.getId());

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);

        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findById(id).orElse(null);
        if (paymentOrder == null) {
            throw new Exception("Payment order not found");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorPayPaymentLink(UserDTO user, Long amount, Long orderId) throws RazorpayException {
        Long paymentAmount = amount * 100;
            RazorpayClient client = new RazorpayClient(razorPayApiKey, razorPaySecretKey);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", paymentAmount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());

        paymentLinkRequest.put("customer", customer);

        //want to notify to user by email or sms;
        JSONObject notify = new JSONObject();
        notify.put("email", true);

        //whenever user click on payment, payment link will be automatically generated and
        // it will redirect user to payment page and
        //also it will send one email to that user with payment link
        // if somehow user close the payment page tab then he can go to email and click on payment link
        paymentLinkRequest.put("notify", notify);

        //you can send remainder as well
        paymentLinkRequest.put("reminder_enable", true);

        //callback url
        //upon successful payment, redirect to user to this url
        paymentLinkRequest.put("callback_url", "https://localhost:3000/payment-success/" + orderId);

        paymentLinkRequest.put("callback_method", "get");

        PaymentLink paymentLink = client.paymentLink.create(paymentLinkRequest);
        return paymentLink;
    }

    @Override
    public String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey=stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("https://localhost:3000/payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("USD")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("salon appointment booking").build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient client = new RazorpayClient(razorPayApiKey, razorPaySecretKey);

                Payment payment = client.payments.fetch(paymentId);

                //confirm payment is done or not-
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")) {
                    //PRODUCE KAFKA EVENT
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS); // we need to perform here other tasks aswell like confirm the booking or create notification- by using kafka/RabbitMQ
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                }
            } else {
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;

            }
        }
        return false;
    }
}
