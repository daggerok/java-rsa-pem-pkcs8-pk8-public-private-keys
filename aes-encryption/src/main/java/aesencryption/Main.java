package aesencryption;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * mvn -q ; java -cp target/classes aesencryption.Main --encrypt=ololo
 *
 * mvn -q ; java -cp target/classes aesencryption.Main --decrypt=9T3d2eg/O8ntHYO1UXdDZg==
 */
public class Main {

  static {
    System.setProperty("java.util.logging.SimpleFormatter.format",
                       "%1$tF %1$tT | %4$-7s | %5$s %n");
  }

  static final Logger log = Logger.getGlobal();

  @SneakyThrows
  public static void main(String[] args) {

    // passed program arguments
    var arguments = Arrays.stream(args)
                          .map(s -> s.split("="))
                          .filter(strings -> strings.length == 2)
                          .collect(Collectors.toMap(strings -> strings[0],
                                                    strings -> strings[1]));

    // encrypt
    var toBeEncoded = arguments.getOrDefault("--encrypt", null);
    Optional.ofNullable(toBeEncoded)
            .map(Main::encrypt)
            .ifPresent(res -> log.info(toBeEncoded + " => " + res));

    // decrypt
    var toBeDecode = arguments.getOrDefault("--decrypt", null);
    Optional.ofNullable(toBeDecode)
            .map(Main::decrypt)
            .ifPresent(res -> log.info(toBeDecode + " => " + res));
  }

  @SneakyThrows
  static String encrypt(String decrypted) {
    var cipher = getCipher(Cipher.ENCRYPT_MODE);
    var input = decrypted.getBytes();
    var encrypted = cipher.doFinal(input); // SneakyThrows
    return /* var output = */ Base64.getEncoder().encodeToString(encrypted);
  }

  @SneakyThrows
  static String decrypt(String encrypted) {
    var cipher = getCipher(Cipher.DECRYPT_MODE);
    var input = Base64.getDecoder().decode(encrypted);
    var decrypted = cipher.doFinal(input); // SneakyThrows
    return /* var output = */ new String(decrypted);
  }

  @SneakyThrows
  static Cipher getCipher(int mode) {
    // env
    var systemCharset = Charset.defaultCharset(); // StandardCharsets.UTF_8
    // infrastructure
    var algorithm = "AES";
    var key = "aesEncryptionKey";
    var initVector = "encryptionIntVec";
    var transformation = "AES/CBC/PKCS5PADDING";
    var iv = new IvParameterSpec(initVector.getBytes(systemCharset));
    var secretKey = new SecretKeySpec(key.getBytes(systemCharset), algorithm);
    var cipher = Cipher.getInstance(transformation); // SneakyThrows
    cipher.init(mode, secretKey, iv); // SneakyThrows
    return /* initialized */ cipher;
  }
}
