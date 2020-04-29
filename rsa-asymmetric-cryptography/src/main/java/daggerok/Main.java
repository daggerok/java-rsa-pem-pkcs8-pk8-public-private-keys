package daggerok;

public class Main {
  public static void main(String[] args) {

    var arguments = Args.of(args);

    var publicKeyPath = arguments.find("publicKey").orElse("target/my/public.key");
    var privateKeyPath = arguments.find("privateKey").orElse("target/my/private.key");

    if (arguments.hasCommand("generate")) {

      var keyPair = KeyService.generateKeyPair(4096);
      var publicKey = KeyService.generatePublicKey(keyPair);
      var privateKey = KeyService.generatePrivateKey(keyPair);

      if (!arguments.hasCommand("and")) return;

      if (arguments.hasCommand("write")) {
        KeyService.write(publicKey, publicKeyPath);
        KeyService.write(privateKey, privateKeyPath);
      }
    }

    if (arguments.hasCommand("read")) {
      System.out.println(KeyService.readPublicKey(publicKeyPath));
      System.out.println(KeyService.readPrivateKey(privateKeyPath));
    }

    var query = arguments.find("payload");

    if (arguments.hasCommand("encrypt")) query
        .map(value -> KeyService.encrypt(value, privateKeyPath))
        .ifPresent(System.out::println);

    if (arguments.hasCommand("decrypt")) query
        .map(value -> KeyService.decrypt(value, publicKeyPath))
        .ifPresent(System.out::println);
  }
}
