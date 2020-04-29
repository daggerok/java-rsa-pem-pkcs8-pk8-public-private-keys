package daggerok;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Generate and parse keys.
 * <p>
 * https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
 * https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyService {

  public static PrivateKey readPrivateKey(String filepath) {
    var bytes = FileService.readBytes(filepath);
    var pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
    return Try.of(() -> KeyFactory.getInstance("RSA"))
              .mapTry(keyFactory -> keyFactory.generatePrivate(pkcs8EncodedKeySpec))
              .onFailure(Err.onFailureSystemOutError)
              .getOrElseThrow(Err.reThrow.apply("cannot generate private key from " + filepath));
  }

  public static PublicKey readPublicKey(String filepath) {
    var bytes = FileService.readBytes(filepath);
    var x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
    return Try.of(() -> KeyFactory.getInstance("RSA"))
              .mapTry(keyFactory -> keyFactory.generatePublic(x509EncodedKeySpec))
              .onFailure(Err.onFailureSystemOutError)
              .getOrElseThrow(Err.reThrow.apply("cannot generate public static key from " + filepath));
  }

  public static void write(Key key, String filepath) {
    FileService.write(key.getEncoded(), filepath);
  }

  public static KeyPair generateKeyPair(int length) {
    var keyGen = Try.of(() -> KeyPairGenerator.getInstance("RSA"))
                    .onFailure(Err.onFailureSystemOutError)
                    .getOrElseThrow(Err.reThrow.apply("cannot get instance of RSA KeyPairGenerator"));
    keyGen.initialize(length);
    return keyGen.generateKeyPair();
  }

  public static PrivateKey generatePrivateKey(KeyPair keyPair) {
    return keyPair.getPrivate();
  }

  public static PublicKey generatePublicKey(KeyPair keyPair) {
    return keyPair.getPublic();
  }

  public static String encrypt(String value, String privateKeyPath) {
    var privateKey = readPrivateKey(privateKeyPath);
    var cipher = getCipher();
    Try.run(() -> cipher.init(Cipher.ENCRYPT_MODE, privateKey))
       .onFailure(Err.onFailureSystemOutError)
       .getOrElseThrow(Err.reThrow.apply("cannot init encoder cipher"));
    return Try.of(() -> Base64.getEncoder()
                              .encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8))))
              .onFailure(Err.onFailureSystemOutError)
              .getOrElseThrow(Err.reThrow.apply("cannot encrypt"));
  }

  public static String decrypt(String value, String publicKeyPath) {
    var publicKey = readPublicKey(publicKeyPath);
    var cipher = getCipher();
    Try.run(() -> cipher.init(Cipher.DECRYPT_MODE, publicKey))
       .onFailure(Err.onFailureSystemOutError)
       .getOrElseThrow(Err.reThrow.apply("cannot init decoder cipher"));
    return Try.of(() -> Base64.getDecoder()
                              .decode(value))
              .mapTry(cipher::doFinal)
              .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
              .onFailure(Err.onFailureSystemOutError)
              .getOrElseThrow(Err.reThrow.apply("cannot encrypt"));
  }

  public static Cipher getCipher() {
    return Try.of(() -> Cipher.getInstance("RSA"))
              .onFailure(Err.onFailureSystemOutError)
              .getOrElseThrow(Err.reThrow.apply("cannot create RSA cipher"));
  }
}
