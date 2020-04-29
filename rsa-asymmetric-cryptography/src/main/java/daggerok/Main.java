package daggerok;

public class Main {
  public static void main(String[] args) {

    var arguments = Args.of(args);

    var publicKeyPath = arguments.find("publicKey").orElse("target/my/public.key");
    var privateKeyPath = arguments.find("privateKey").orElse("target/my/private.key");

    if (arguments.is("generate")) {

      var keyPair = KeyService.generateKeyPair(4096);
      var publicKey = KeyService.generatePublicKey(keyPair);
      var privateKey = KeyService.generatePrivateKey(keyPair);

      if (!arguments.is("and")) return;

      if (arguments.is("write")) {
        KeyService.write(publicKey, publicKeyPath);
        KeyService.write(privateKey, privateKeyPath);
      }
    }

    if (arguments.is("read")) {
      System.out.println(KeyService.readPublicKey(publicKeyPath));
      System.out.println(KeyService.readPrivateKey(privateKeyPath));
    }

    var payload = arguments.find("payload");

    if (arguments.is("encrypt")) payload
        .map(value -> KeyService.encrypt(value, privateKeyPath))
        .ifPresent(System.out::println);

    if (arguments.is("decrypt")) payload
        .map(value -> KeyService.decrypt(value, publicKeyPath))
        .ifPresent(System.out::println);
  }
}
