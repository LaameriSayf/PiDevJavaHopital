package Controllers;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Formatter;



public class DigitalSignature {

    public PublicKey publicKeyFromCertificate(String fileName) throws Exception {
        FileInputStream fileInputStream=new FileInputStream(fileName);
        CertificateFactory certificateFactory=CertificateFactory.getInstance("X.509");//Format du certificat
        Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
        //System.out.println("=================================");
        //System.out.println(certificate.toString());
        //System.out.println("=================================");
        return certificate.getPublicKey();
    }
    public PrivateKey privateKeyFromJKS(String fileName, String jksPassWord, String alias) throws Exception {
        FileInputStream fileInputStream=new FileInputStream(fileName);
        KeyStore keyStore=KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(fileInputStream,jksPassWord.toCharArray());
        Key key = keyStore.getKey(alias, jksPassWord.toCharArray());
        PrivateKey privateKey= (PrivateKey) key;
        return privateKey;
    }


    //génerer la signature avec RSA
    public String rsaSign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature=Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey,new SecureRandom());
        signature.update(data);//données
        byte[] sign = signature.sign();//génerer signature
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean rsaSignVerify(String signedDoc,PublicKey publicKey) throws Exception {
        Signature signature=Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        String[] data=signedDoc.split("_.._");
        String document=data[0];
        String sign=data[1];
        byte[] decodeSignature = Base64.getDecoder().decode(sign);
        signature.update(document.getBytes());
        boolean verify = signature.verify(decodeSignature);
        return verify;
    }


}

