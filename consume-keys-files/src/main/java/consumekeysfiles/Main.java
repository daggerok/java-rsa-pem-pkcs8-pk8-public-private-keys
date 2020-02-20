package consumekeysfiles;

import lombok.SneakyThrows;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    var in = Arrays.stream(args)
                   .map(s -> s.split("="))
                   .filter(strings -> strings.length == 2)
                   .collect(Collectors.toMap(strings -> strings[0],
                                             strings -> strings[1]));

    var publicKeyParam = in.getOrDefault("--public", "public_key.pem");
    var privateKeyParam = in.getOrDefault("--private", "private_key.pem");

    var pathToPublic = Paths.get(publicKeyParam);
    var pathToPrivate = Paths.get(privateKeyParam);

    // var systemCharset = StandardCharsets.UTF_8;
    var systemCharset = Charset.defaultCharset();
    log.info("charset: '" + systemCharset.toString() + "'");
    var publicKeyRawContent = Files.readString(pathToPublic, systemCharset);
    var privateKeyRawContent = Files.readString(pathToPrivate, systemCharset);

    var lineSeparator = System.lineSeparator();
    log.info("line separator: '" + lineSeparator + "'");
    // var lineSeparator = System.getProperty("os.name").toLowerCase().contains("win") ? "\\r\\n" : "\\n";
    var publicKey = publicKeyRawContent // fix Illegal base64 characters...
                                       .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                                       .replaceAll("-----END PUBLIC KEY-----", "")
                                        // .replaceAll("$|\n|\r\n|\n |\r\n | \n| \r\n", "")
                                        .replaceAll(lineSeparator, "");
    log.info(publicKey);
    var privateKey = privateKeyRawContent.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                                         .replaceAll("-----END PRIVATE KEY-----", "")
                                         // .replaceAll("$|\n|\r\n|\n |\r\n | \n| \r\n", "")
                                         .replaceAll(lineSeparator, "");
    log.info(privateKey);

    var decoder = Base64.getDecoder();
    var x509EncodedPublicKeySpec = new X509EncodedKeySpec(decoder.decode(publicKey));
    var pkcs8EncodedPrivateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(privateKey));

    var keyFactory = KeyFactory.getInstance("RSA");
    var rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedPublicKeySpec);
    var rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedPrivateKeySpec);

    log.info("RSA public key:");
    log.info(rsaPublicKey.toString());
    log.info("RSA private key:");
    log.info(rsaPrivateKey.toString());
  }
}
