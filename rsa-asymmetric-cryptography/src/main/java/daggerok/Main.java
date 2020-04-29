package daggerok;

public class Main {
  public static void main(String[] args) {

    var arguments = Args.of(args);

    if (arguments.has("help")
        || arguments.has("-h")
        || arguments.has("-?")
        || arguments.has("/?")
        || arguments.has("--help")) {
      System.out.println("<main> <command> [--<query>=<value>]");
      System.out.println("");
      System.out.println("examples:");
      System.out.println("\tdaggerok.Main generate");
      System.out.println("\tdaggerok.Main generate and write");
      System.out.println("\tdaggerok.Main generate and write --publicKey=/path/to/public.key");
      System.out.println("\tdaggerok.Main read");
      System.out.println("\tdaggerok.Main encrypt --payload=\"Hello, World\"");
      return;
    }

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
