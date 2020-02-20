# java and public / private keys [![CI](https://github.com/daggerok/java-rsa-pem-pkcs8-pk8-public-private-keys/workflows/CI/badge.svg)](https://github.com/daggerok/java-rsa-pem-pkcs8-pk8-public-private-keys/actions)

```bash
./mvnw clean compile

openssl genrsa -out consume-keys-files/target/private_rsa_key.pem 4096
openssl rsa -pubout -in consume-keys-files/target/private_rsa_key.pem -out consume-keys-files/target/public_key.pem
openssl pkcs8 -topk8 -in consume-keys-files/target/private_rsa_key.pem -inform pem -out consume-keys-files/target/private_key.pem -outform pem -nocrypt

java -cp consume-keys-files/target/classes consumekeysfiles.Main --public=consume-keys-files/target/public_key.pem --private=./consume-keys-files/target/private_key.pem
```

[TODO: Write app to generate keys (files?) from java for later use](https://www.devglan.com/java8/rsa-encryption-decryption-java)
