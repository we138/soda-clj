#!/bin/sh

echo "=== Starting application... ===";

lein migrate
java -jar /app/target/uberjar/soda-clj-0.1.0-SNAPSHOT-standalone.jar
