package consumekeysfiles;

import lombok.SneakyThrows;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Generate keys before you start...
 * <p>
 * rm -rf *.pem
 * openssl genrsa -out private_rsa_key.pem 4096
 * openssl rsa -pubout -in private_rsa_key.pem -out public_key.pem
 * openssl pkcs8 -topk8 -in private_rsa_key.pem -inform pem -out private_key.pem -outform pem -nocrypt
 */
public class Main {

  static {
    System.setProperty("java.util.logging.SimpleFormatter.format",
                       "%1$tF %1$tT | %4$-7s | %5$s %n");
  }

  private static final Logger log = Logger.getGlobal();

  /*
   * ./mvnw compile
   * java -cp target/classes consumekeysfiles.Main --public=$(pwd)/public_key.pem --private=`pwd`/private_key.pem
   */
  @SneakyThrows
  public static void main(String[] args) {
    // environment
    var systemCharset = Charset.defaultCharset(); // StandardCharsets.UTF_8
    var lineSeparator = System.lineSeparator();
    log.info("line separator: '" + lineSeparator + "'");
    log.info("charset: '" + systemCharset.toString() + "'");
    // var lineSeparator = System.getProperty("os.name").toLowerCase().contains("win") ? "\\r\\n" : "\\n";

    // infrastructure
    var decoder = Base64.getDecoder();
    var keyFactory = KeyFactory.getInstance("RSA");

    // passed program arguments
    var arguments = Arrays.stream(args)
                          .map(s -> s.split("="))
                          .filter(strings -> strings.length == 2)
                          .collect(Collectors.toMap(strings -> strings[0],
                                                    strings -> strings[1]));

    // public key
    var publicKeyPath = arguments.getOrDefault("--public", "public_key.pem");
    var publicKey = getPublicKey(publicKeyPath, systemCharset, lineSeparator);
    var rsaPublicKey = getRsaPublicKey(publicKey, decoder, keyFactory);

    log.info("Public key algorithm: " + rsaPublicKey.getAlgorithm());
    log.info("Public key format: " + rsaPublicKey.getFormat());
    log.info(publicKey);

    // private key
    var privateKeyPath = arguments.getOrDefault("--private", "private_key.pem");
    var privateKey = getPrivateKey(privateKeyPath, systemCharset, lineSeparator);
    var rsaPrivateKey = getRsaPrivateKey(privateKey, decoder, keyFactory);

    // log.info(rsaPrivateKey.toString());
    log.info("Private key algorithm: " + rsaPrivateKey.getAlgorithm());
    log.info("Private key format: " + rsaPrivateKey.getFormat());
    // log.info("  => modulus: " + rsaPrivateKey.getModulus());
    // log.info("  => exponent: " + rsaPrivateKey.getPrivateExponent());
    // log.info("  => params: " + rsaPrivateKey.getParams());
    log.info(privateKey);
  }

  @SneakyThrows
  static String getPublicKey(String publicKeyPath, Charset systemCharset, String lineSeparator) {
    var pathToPublic = Paths.get(publicKeyPath);
    var publicKeyRawContent = Files.readString(pathToPublic, systemCharset); // SneakyThrows
    return /* var publicKey = */ publicKeyRawContent // fix Illegal base64 characters...
                                                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                                                    .replaceAll("-----END PUBLIC KEY-----", "")
                                                    // .replaceAll("$|\n|\r\n|\n |\r\n | \n| \r\n", "")
                                                    .replaceAll(lineSeparator, "");
  }

  @SneakyThrows
  static RSAPublicKey getRsaPublicKey(String publicKey, Base64.Decoder decoder, KeyFactory keyFactory) {
    var x509EncodedPublicKeySpec = new X509EncodedKeySpec(decoder.decode(publicKey));
    return /* var rsaPublicKey = */ (RSAPublicKey) keyFactory.generatePublic(x509EncodedPublicKeySpec); // SneakyThrows
  }

  @SneakyThrows
  static String getPrivateKey(String privateKeyPath, Charset systemCharset, String lineSeparator) {
    var pathToPrivate = Paths.get(privateKeyPath);
    var privateKeyRawContent = Files.readString(pathToPrivate, systemCharset); // SneakyThrows
    return /* var privateKey = */ privateKeyRawContent.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                                                      .replaceAll("-----END PRIVATE KEY-----", "")
                                                      // .replaceAll("$|\n|\r\n|\n |\r\n | \n| \r\n", "")
                                                      .replaceAll(lineSeparator, "");
  }

  @SneakyThrows
  static RSAPrivateKey getRsaPrivateKey(String privateKey, Base64.Decoder decoder, KeyFactory keyFactory) {
    var pkcs8EncodedPrivateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(privateKey));
    return /* var rsaPrivateKey = */ (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedPrivateKeySpec); // SneakyThrows
  }
}
