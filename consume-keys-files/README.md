# consume keys files from java

```bash
mvn clean compile

openssl genrsa -out target/private_rsa_key.pem 4096
openssl rsa -pubout -in target/private_rsa_key.pem -out target/public_key.pem
openssl pkcs8 -topk8 -in target/private_rsa_key.pem -inform pem -out target/private_key.pem -outform pem -nocrypt

java -cp target/classes consumekeysfiles.Main --public=target/public_key.pem --private=./target/private_key.pem
```
