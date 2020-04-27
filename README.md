# java certificates and public / private keys [![CI](https://github.com/daggerok/java-rsa-pem-pkcs8-pk8-public-private-keys/workflows/CI/badge.svg)](https://github.com/daggerok/java-rsa-pem-pkcs8-pk8-public-private-keys/actions)

## rsa-asymmetric-cryptography

TODO...

```bash
./mvnw -f rsa-asymmetric-cryptography clean package dependency:copy-dependencies -DincludeScope=runtime
java -cp "rsa-asymmetric-cryptography/target/dependency/*:rsa-asymmetric-cryptography/target/classes" daggerok.Main  
```

* [link 1](https://stackoverflow.com/questions/8310539/how-to-copy-dependencies-jars-without-test-jars-to-a-directory-using-maven/61456534#61456534)
* [link 2](https://mkyong.com/java/java-asymmetric-cryptography-example/)

## consume-keys-files

```bash
./mvnw clean compile -f consume-keys-files

openssl genrsa -out consume-keys-files/target/private_rsa_key.pem 4096
openssl rsa -pubout -in consume-keys-files/target/private_rsa_key.pem -out consume-keys-files/target/public_key.pem
openssl pkcs8 -topk8 -in consume-keys-files/target/private_rsa_key.pem -inform pem -out consume-keys-files/target/private_key.pem -outform pem -nocrypt

java -cp consume-keys-files/target/classes consumekeysfiles.Main --public=consume-keys-files/target/public_key.pem --private=./consume-keys-files/target/private_key.pem
```

[TODO: Write app to generate keys (files?) from java for later use](https://www.devglan.com/java8/rsa-encryption-decryption-java)

## aes-encryption

```bash
./mvnw clean compile -f aes-encryption

java -cp aes-encryption/target/classes aesencryption.Main --encrypt=ololo
java -cp aes-encryption/target/classes aesencryption.Main --decrypt=9T3d2eg/O8ntHYO1UXdDZg==
```

[AES in java](https://www.devglan.com/corejava/java-aes-encypt-decrypt)

## rsa-key-pair-generation

```bash
./mvnw clean compile -f rsa-key-pair-generation

java -cp `pwd`/rsa-key-pair-generation/target/classes rsakeypairgeneration.Main --keySize=1024
```

[RSA Encryption in Javascript and Decryption in Java](https://www.devglan.com/spring-mvc/rsa-encryption-in-javascript-and-decryption-in-java)
