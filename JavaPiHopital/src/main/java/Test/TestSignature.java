package Test;

import Controllers.DigitalSignature;

public class TestSignature {
    public static void main(String[] args) throws Exception {
        DigitalSignature Signature =new DigitalSignature();
        String secret="Nom Medecin ";
        String document="This is my message";
        String signature = Signature.hmacSign(document.getBytes(), secret);
        String signedDocument=document+"_.._"+signature;
        System.out.println(signedDocument);
        System.out.println("===========================");
        String signedDoc="This is my message_.._EruuhEL5ZBEwysijCZx0YeMNaBhnuVLbXErVq4231W8=";
        String sec="azerty";
        System.out.println("Signature verification");
        boolean signatureVerifResult = Signature.hmacVerify(signedDoc, secret);
        System.out.println(signatureVerifResult==true?"Signature OK":"Signature Not OK");




    }


}