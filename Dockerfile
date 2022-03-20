FROM clojure:openjdk-17-lein-slim-buster

WORKDIR /app

COPY . /app

RUN lein deps && lein uberjar