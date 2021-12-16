This is a project to implement a Json Web Token based authentication to consume a
REST API written with Java Spring Boot. The first part was directly using hardcoded
Java singleton objects instead of a database to just experiment with the Jwt implementation.
I plan to use a SQLite database in the future (work in progress).
https://www.baeldung.com/spring-boot-sqlite


https://jwt.io/

BUILD
=====

mvn clean

mvn spring-boot:run


TEST
===

POST application/json
https://localhost:3000/api/authenticate

{
	"username": "fabrice",
	"password": "1234"
}

RESPONSE 200 OK:
{
	"token": "<header.payload.signature>"
}

Use the JWT token <header.payload.signature> received after authentication to consume the API

GET
Bearer <header.payload.signature>
https://localhost:3000/api/entries

Should return a 200 OK with the expected entries from the backend as application/json

If the token is invalid, you'll get a 401 Unauthorized. (See also refresh token case in the NOTES)


NOTES
=====

1. Spring Boot Security Example - Refresh Expired JSON Web Token
Some code was written based on this implementation:
https://www.javainuse.com/webseries/spring-security-jwt/chap7

In this example Spring Boot + JWT. I test the topic of JWT Expiration.
I had implemented the solution such that if the JWT has expired
then the JWTExpiredException is thrown, and the user gets a 401 Unauthorized.

Then the user is forced to login again.
But suppose that if the token has expired, still the user should be allowed to access the system if the token is valid
but expired in order to skip having to login again.

Upon expiration of a token (that can be detected in the client too), a new valid token is generated.

To test, tweak the values in application.properties:
# jwt.tokenValidityInSeconds=3600
# Set the validity of the first token received to 30 seconds
jwt.tokenValidityInSeconds=30
# Set the validity of a new refreshed token to 2 hours
jwt.refreshExpirationDateInSeconds=7200

Do authorize with POST as above and save the token.
Try to fetch entries until token has expired (You'll get 401 Unauthorized).
But since the client knows that the token has expired, it can also try to refresh the token:

GET
https://localhost:3000/api/refreshtoken
+Bearer <header.payload.signature of the expired token>
+Header isRefreshToken: true
RESPONSE 200 OK:
{
	"token": "<header.payload.signature of the NEW token>"
}

Since the first token expired after 30 seconds but the new token is valid for 2 hours,
it's easy to verify that it is working.

========

2. An exception occured because I use a too new version of Java:

java.lang.ClassNotFoundException: javax.xml.bind.DatatypeConverter
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641) ~[na:na]
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188) ~[na:na]
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520) ~[na:na]
        at io.jsonwebtoken.impl.Base64Codec.decode(Base64Codec.java:26) ~[jjwt-0.9.1.jar:0.9.1]
...

Java 11 and above require more stuff unfortunately. javax.xml.bind.DatatypeConverter is removed in JDK11

I have some code that uses JAXB API classes which have been provided as a part of the JDK in Java 6/7/8. When I run the same code with Java 9, at runtime I get errors indicating that JAXB classes can not be found.

The JAXB classes have been provided as a part of the JDK since Java 6, so why can Java 9 no longer find these classes?

What you will need to do in Java 11 and forward is include your own copy of the Java EE APIs on the classpath or module path. For example, you can add the JAX-B APIs as a Maven dependency like this:

<!-- API, java.xml.bind module -->
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>2.3.2</version>
</dependency>

<!-- Runtime, com.sun.xml.bind module -->
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.2</version>
</dependency>
