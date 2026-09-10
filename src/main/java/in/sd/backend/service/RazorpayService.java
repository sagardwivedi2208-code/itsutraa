package in.sd.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

@Service
public class RazorpayService {
    @Value("${razorpay.key.id:}") private String keyId;
    @Value("${razorpay.key.secret:}") private String keySecret;
    private final RestClient client = RestClient.builder().baseUrl("https://api.razorpay.com/v1").build();

    public String getKeyId(){ return keyId; }
    public Map<String,Object> createOrder(int amountPaise, String receipt) {
        if(keyId.isBlank() || keySecret.isBlank()) throw new IllegalStateException("Razorpay keys are not configured");
        return client.post().uri("/orders").headers(h -> h.setBasicAuth(keyId,keySecret)).contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("amount",amountPaise,"currency","INR","receipt",receipt)).retrieve().body(Map.class);
    }
    public boolean verify(String orderId,String paymentId,String signature) throws Exception {
        Mac mac=Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
        String expected=HexFormat.of().formatHex(mac.doFinal((orderId+"|"+paymentId).getBytes(StandardCharsets.UTF_8)));
        return expected.equalsIgnoreCase(signature);
    }
}
