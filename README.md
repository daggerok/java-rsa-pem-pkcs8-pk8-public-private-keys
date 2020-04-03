# java certificates and public / private keys [![CI](https://github.com/daggerok/java-rsa-pem-pkcs8-pk8-public-private-keys/workflows/CI/badge.svg)](https://github.com/daggerok/java-rsa-pem-pkcs8-pk8-public-private-keys/actions)

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
