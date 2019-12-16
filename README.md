Datatype module to make [Jackson](../../../jackson)
recognize `JsonValue` types of JSON API defined in JSR-353 ("JSON-Processing"), so that
you can read JSON as `JsonValue`s and write `JsonValue`s as JSON as part of normal
Jackson processing.

Note that this module DOES NOT actually IMPLEMENT `jsr-353` specification.
There is one known [jackson-javax-json](https://github.com/pgelinas/jackson-javax-json)
project that actually *implements* JSR-353 using Jackson streaming API under the hood,
although it is not actively maintained any more.

The main reason for using this module is interoperability, as well as to take advantage
of powerful data-binding features Jackson provides.
Another benefit is the performance: Jackson implementation is often significantly faster for
reading and writing JSON content than Oracle's JSR-353 Reference Implementation.

## Status

[![Build Status](https://travis-ci.org/FasterXML/jackson-datatype-jsr353.svg)](https://travis-ci.org/FasterXML/jackson-datatype-jsr353)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.fasterxml.jackson.datatype/jackson-datatype-jsr353/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.fasterxml.jackson.datatype/jackson-datatype-jsr353/)
[![Javadoc](https://javadoc.io/badge/com.fasterxml.jackson.datatype/jackson-datatype-jsr353.svg)](http://www.javadoc.io/doc/com.fasterxml.jackson.datatype/jackson-datatype-jsr353)

As of 2.3 module is considered stable and production ready.

## Usage

### Maven dependency

To use module on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jsr353</artifactId>
  <version>2.9.0</version>
</dependency>
```

(or whatever version is most up-to-date at the moment)

Also unless you already include a dependency to a JSR-353 implementation (JDK does not ship with one at least with JDK 8
and prior), you may need to include one. Implementations include:

* [JSR 353 Reference Implementation](https://jsonp.java.net/)
* [Jackson-javax-json](https://github.com/pgelinas/jackson-javax-json)

Reference implementation (last updated in 2013) dependency would be:

```xml
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>javax.json</artifactId>
    <version>1.0.4</version>
</dependency>
```

### Registering module

Like all standard Jackson modules (libraries that implement Module interface), registration is done as follows:

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JSR353Module());
```

after which functionality is available for all normal Jackson operations:
you can read JSON as `JsonValue` (or its subtypes), `JsonValues` as JSON, like:

```java
JsonObject ob = mapper.readValue(JSON, JsonObject.class);
mapper.writeValue(new File("stuff.json"), ob);
```

## More

See [Wiki](../../wiki) for more information (javadocs, downloads).
