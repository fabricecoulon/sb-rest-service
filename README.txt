This is a project to implement a Json Web Token based authentication to consume a
REST API written with Java Spring Boot. The first part was directly using hardcoded
Java singleton objects instead of a database to just experiment with the Jwt implementation.
I plan to use a SQLlite database in the future (work in progress).

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
	"token": "<...>"
}

Use the JWT token <...> received after authentication to consume the API

GET
Bearer Token <...>
https://localhost:3000/api/entries

Should return a 200 OK with the expected entries from the backend as application/json

If the token is invalid, you'll get a 401 Unauthorized


NOTES
=====

java.lang.ClassNotFoundException: javax.xml.bind.DatatypeConverter
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641) ~[na:na]
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188) ~[na:na]
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520) ~[na:na]
        at io.jsonwebtoken.impl.Base64Codec.decode(Base64Codec.java:26) ~[jjwt-0.9.1.jar:0.9.1]
...

Java 11 requires more stuff unfortunately. javax.xml.bind.DatatypeConverter is removed in JDK11

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
