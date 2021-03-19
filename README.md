---
 http://localhost:8080/security/swagger-ui.html
---

Use the following java keytool (shipped with the JDK) command to generate a key pair.

keytool -genkey -alias <alias> -keyalg RSA -keystore <keystore_name>  -keysize 2048
### alias — A name to uniquely identify the generated keypair entry within the generate keystore. Let’s use “jwtsigning”
### keyalg — Public key algorithm. Should be “RSA” for our case.
### keystore — A name for the keystore file generated. Let’s use “keystore.jks”
### keysize — Size (a measure of strength) of the generated public key. We should set that to 2048 at least. Can be set to 4096 for better security (to further reduce the possibility of an attacker guessing your keys).

I’m using the following command to generate the keypair and move on with our solution:
 keytool -genkey -alias chrisgyaspsec -keyalg RSA -keystore keystore_chrisgya.jks  -keysize 4096

References
### https://ordina-jworks.github.io/monitoring/2020/11/16/monitoring-spring-prometheus-grafana.html

### INVESTIGATE THE LINKS BELOW
---
 https://github.com/rieckpil/blog-tutorials/blob/master/spring-boot-datajpatest/src/test/java/de/rieckpil/blog/OrderRepositoryShortTest.java
 https://dzone.com/articles/spring-boot-rabbitmq-tutorial-retry-and-error-hand

---
https://www.baeldung.com/spring-data-jpa-query

https://attacomsian.com/blog/spring-data-jpa-specifications
---

---
https://www.c-sharpcorner.com/article/how-to-create-and-use-procedure-and-function-postgresql/
---