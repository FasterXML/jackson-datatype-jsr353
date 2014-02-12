Datatype module to make Jackson (http://jackson.codehaus.org) 
recognize `JsonValue` types of JSON API defined in JSR-353 ("JSON-Processing"), so that
you can read JSON as `JsonValue`s and write `JsonValue`s as JSON as part of normal
Jackson processing.

Note that there is also a related [jackson-javax-json](https://github.com/pgelinas/jackson-javax-json)
module which actually *implements* JSR-353 using Jackson streaming API under the hood.

In both cases the main reason for use is interoperability, as well as to take advantage
of powerful data-binding features Jackson provides.
Another benefit is the
performance: Jackson implementation is often significantly faster for reading and writing
JSON content than Oracle's JSR-353 Reference Implementation.

## Status

First official version, 2.2.0, was released in April 2013.
Module is still considered experimental, but should be usable given its simplicity.

## Usage

### Maven dependency

To use module on Maven-based projects, use following dependency:

```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jsr353</artifactId>
  <version>2.2.0</version>
</dependency>
```

(or whatever version is most up-to-date at the moment)

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

See [Wiki](jackson-datatype-jsr353/wiki) for more information (javadocs, downloads).
