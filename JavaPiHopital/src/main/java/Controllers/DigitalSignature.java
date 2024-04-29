package Controllers;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DigitalSignature {

    public static String hmacSign(byte[] data, String privateSecret) throws Exception {
        SecretKeySpec secretKeySpec=new SecretKeySpec(privateSecret.getBytes(),"HmacSHA256");
        Mac mac=Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signature = mac.doFinal(data);
        return Base64.getEncoder().encodeToString(signature);

        }
    public boolean hmacVerify(String signedDocument,String secret) throws Exception {
        SecretKeySpec secretKeySpec=new SecretKeySpec(secret.getBytes(),"HmacSHA256");
        Mac mac=Mac.getInstance("HmacSHA256");
        String[] splitedDocument=signedDocument.split("_.._");
        String document=splitedDocument[0];
        String documentSignature=splitedDocument[1];
        mac.init(secretKeySpec);
        byte[] sign = mac.doFinal(document.getBytes());
        String base64Sign=Base64.getEncoder().encodeToString(sign);
        return (base64Sign.equals(documentSignature));
    }

    }

