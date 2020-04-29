package daggerok;

public class Main {
  public static void main(String[] args) {

    var arguments = Args.of(args);

    var publicKeyPath = arguments.find("publicKey").orElse("target/my/public.key");
    var privateKeyPath = arguments.find("privateKey").orElse("target/my/private.key");

    if (arguments.has("generate")) {

      var keyPair = KeyService.generateKeyPair(4096);
      var publicKey = KeyService.generatePublicKey(keyPair);
      var privateKey = KeyService.generatePrivateKey(keyPair);

      if (!arguments.has("and")) return;

      if (arguments.has("write")) {
        KeyService.write(publicKey, publicKeyPath);
        KeyService.write(privateKey, privateKeyPath);
      }
    }

    if (arguments.has("read")) {
      System.out.println(KeyService.readPublicKey(publicKeyPath));
      System.out.println(KeyService.readPrivateKey(privateKeyPath));
    }

    var query = arguments.find("payload");

    if (arguments.has("encrypt")) query
        .map(value -> KeyService.encrypt(value, privateKeyPath))
        .ifPresent(System.out::println);

    if (arguments.has("decrypt")) query
        .map(value -> KeyService.decrypt(value, publicKeyPath))
        .ifPresent(System.out::println);
  }
}
