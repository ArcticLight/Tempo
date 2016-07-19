# Tempo

A demonstration in packing the LWJGL natives in with a jar and hotpatching that jar to run correctly. This solution packs the natives in with the fat jar, and knows how to inflate them from itself by using the classpath. It then goes on to mangle the classpath to demonstrate that you can start a fat jar packed in this manner just by double-clicking on it.

This is mostly a packaging problem (and therefore its associated solution), rather than a programming problem

This was developed with Java 7 in mind, and there are no guarantees that the dirty hack that makes the entire solution work will:

1. work on any other Java version
2. work on any other JVM than the official Oracle JVM
3. work at all.
