# Dependency Finder

This project is a dependency analysis tool developed as part of my application for the JetBrains Internship program. The tool uses the ClassGraph library to analyze and validate dependencies within a Java/Kotlin project, identifying missing dependencies for specified classes across provided JAR files.
Overview

Dependency Finder allows you to verify that all dependencies required by a specific class are satisfied by a given list of JAR files. If any dependencies are missing, the tool will indicate them, providing an easy way to manage external dependencies and ensure successful class execution.
Key Features

    Inter-Class Dependency Analysis: Scans bytecode to identify dependencies for fields, methods, superclasses, and interfaces.
    Missing Dependency Reporting: Identifies dependencies not satisfied by the provided JAR files.
    JUnit 5 Testing: Includes unit tests to validate various scenarios and use cases.
    Gradle-Based: Built using Gradle for straightforward build and dependency management.

## Project Structure

    .
    ├── src
    │   ├── main
    │   │   ├── kotlin
    │   │   │   └── DependencyFinder.kt         # Main dependency analysis class
    │   └── test
    │       └── kotlin
    │           └── DependencyFinderTest.kt     # Unit tests for DependencyFinder
    ├── build.gradle                            # Gradle build file
    └── settings.gradle                         # Gradle settings file

## Prerequisites

    JDK 11+
    Gradle (or use the Gradle Wrapper included in the project)
    JUnit 5 (configured via Gradle dependencies)
    ClassGraph library

## Setup 


Add Required JAR Files for testing:
specify path to the following jars in the test file

    ModuleA-1.0.jar
    ModuleB-1.0.jar
    commons-io-2.16.1.jar

### Build the Project: Use Gradle to build the project:


    ./gradlew build

## Usage

This project requires 2 parameters which are input like this after project is build:

    ./gradlew run --args=" <MainClassName> <Path to JarFile1> <Path to JarFile2> ..."

## Example Usage

Running Tests

This project includes JUnit 5 test cases to verify functionality. To run the tests, you can use the Gradle test task:

bash

    ./gradlew test
