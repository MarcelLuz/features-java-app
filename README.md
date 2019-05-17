# Features-Java-App
A java command line tool to extract Machine Learning Features from Java Repos or single Files.

This App uses two different extractor (JAR's) for Feature creation:

* features-javac
  * (A javac plugin for extracting a feature graph for plugging in to machine learning models)
  * source of the original javac plugin: https://github.com/acr31/features-javac
  * modified version: https://github.com/MarcelLuz/features-java-graph
  * Licence: Apache-2.0
  
* java-extractor (paths)
  * (A javac plugin for extracting a feature graph for plugging in to machine learning models)
  * source of the original extractor: https://github.com/tech-srl/code2seq/tree/master/JavaExtractor
  * modified version: https://github.com/MarcelLuz/java-path-extractor
  * Licence: MIT

## features-javac
Extract Graph's from Java Files (in .proto)

Can be used to predict variable names or method names.

## java-extractor
Extracts the AST-Paths form Java-Methods.

Can be used to predict method names or generate java-doc

## Prerequisite

JDK 1.12 min language-level 1.8+
Maven 3.5+

## Configuration
app.properties contains all settings. 

## Licence
Apache-2.0