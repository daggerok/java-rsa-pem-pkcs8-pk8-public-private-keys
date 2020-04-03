package rsakeypairgeneration;

import lombok.SneakyThrows;
import lombok.Value;

import java.security.KeyPairGenerator;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * mvn ; java -cp target/classes rsakeypairgeneration.Main --keySize=1024
 */
public class Main {

  static {
    System.setProperty("java.util.logging.SimpleFormatter.format",
                       "%1$tF %1$tT | %4$-7s | %5$s %n");
  }

  static final Logger log = Logger.getGlobal();

  public static void main(String[] args) {

    var arguments = Arrays.stream(args)
                          .map(s -> s.split("="))
                          .filter(strings -> strings.length == 2)
                          .collect(Collectors.toMap(strings -> strings[0],
                                                    strings -> strings[1]));

    var keySize = arguments.getOrDefault("--keySize", "512"); // 8192
    var keysPair = generateRsaKeysPair(keySize);

    log.info("public:\n" + keysPair.publicKey);
    log.info("private:\n" + keysPair.privateKey);
  }

  @SneakyThrows
  static Pair generateRsaKeysPair(String keySize) {
    var keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(Optional.ofNullable(keySize)
                              .map(Integer::valueOf)
                              // .orElse(4096));
                              .orElse(512));
    var pair = keyGen.generateKeyPair();
    var publicKey = pair.getPublic();
    var privateKey = pair.getPrivate();
    var encoder = Base64.getEncoder();
    return new Pair(encoder.encodeToString(publicKey.getEncoded()),
                    encoder.encodeToString(privateKey.getEncoded()));
  }

  @Value//(staticConstructor = "of")
  static class Pair {
    String publicKey, privateKey;
  }
}
