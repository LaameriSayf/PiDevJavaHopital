package Test;

import Controllers.DigitalSignature;
import Controllers.sign_pdf;

import java.security.PrivateKey;
import java.security.PublicKey;

public class TestRsaSign {
    public static void main(String[] args) throws Exception {
        DigitalSignature Dsig =new DigitalSignature();
        PrivateKey privateKey= Dsig .privateKeyFromJKS("esprit.jks","123456","noura");
        String data="This is my message {}=>";
        String signature = Dsig .rsaSign(data.getBytes(), privateKey);
        String signedDoc=data+"_.._"+signature;
        System.out.println(signedDoc);
        System.out.println("===================================");
        System.out.println("Signature verification");
        String signedDocRecived="This is my message {}=>_.._b57383c6O5L8Old3jO8WOVpwWnx5TR+Tx11UjvIQehI5NuHwnTqRTdqdJWDYmgd5tvHk5BlCSvpXQm6cRuW6v16gNuf1pnyQZFyEatEn7nfvd/PVlGRQd9y4T60O6o+Box0UjnHt5Sx0L2bDroUW/2Xkvx1cujKo8sZSnvU0l9B49UUOEBsmXbjiagwqXa1/5gtFKy46VIzQ0Cl0hNA/ueXQM53pY8xJs4ky+4R6oyweT91qVVjj46jr77SDiymmt6R/yxF+Rs+bzlNudffblgChDOf8+Xc893EJdkp5m7fSTaVkPR3wk+vqz8ZLANiOGH/LwyJBWE0NZYmXeBEHQQ==";
        PublicKey publicKey=Dsig .publicKeyFromCertificate("Certificate.cert");
        boolean b = Dsig .rsaSignVerify(signedDocRecived, publicKey);
        System.out.println(b?"Signature OK":"Signature Not OK");
    }




}
