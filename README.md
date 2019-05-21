# Java-Preprocessor-App
A java command line tool to preprocess java files for machine learning.
It can process single or multiple files in a repo at the same time.

This App uses two different extractor (Lib's(JAR's)) for feature extraction:

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
Extract graph's from java files (in .proto)

Can be used to predict variable names or method names.

## java-extractor
Extracts the AST-paths form java methods.

Can be used to predict method names or generate java-doc

## Prerequisite

JDK 1.12+

Maven 3.5+

## Configuration
app.properties contains all settings. 

## Licence
Apache-2.0
