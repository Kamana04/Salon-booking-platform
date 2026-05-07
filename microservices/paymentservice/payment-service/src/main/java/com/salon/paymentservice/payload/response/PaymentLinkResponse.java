package com.salon.paymentservice.payload.response;

import lombok.Data;

@Data
public class PaymentLinkResponse {

    private String paymentLinkUrl;

    private String getPaymentLinkId;
}
