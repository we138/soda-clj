

# Soda

it's like a `Lemonade`


## Running

    docker-compose up

Application will be available on `http://0.0.0.0:3000`


## API


### Schedule webhook

    curl --location --request POST 'http://0.0.0.0:3000/timers' \
         --header 'Content-Type: application/json' \
         --data-raw '{"url": "http://google.com", "hours": 0, "minutes": 0, "seconds": 30}'

The application should return json:

    {
      "id": 1
    }


### Fetch webhook

    curl --location --request GET 'http://0.0.0.0:3000/timers/1' \
         --header 'Content-Type: application/json' \
         --data-raw '{"url": "http://google.com", "hours": 0, "minutes": 0, "seconds": 30}'

The application should return json:

    {
        "id": 1,
        "time_left": 3
    }


## Architecture

Application written on Clojure, this is functional language with pretty good concurrency and java interop.
Below, I will describe how it works.


### System

`./src/soda_clj/system.clj`

Is a heart of application. Here are all application settings,
dependencies and rules how to load and stop components (such as databases, webservers, etc.).


### Router

`./src/soda_clj/router`

Router serves for storing JSON API routes, parameters coercion, exception handling.


### Handlers

`./src/soda_clj/handlers`

Component for describing API endpoints handlers. Params validation, response serialization, calling business logic.


### Schemas

`./src/soda_clj/schemas`

Namespace with schemas contains schema definitions for params validation in handlers.


### DB

`./src/soda_clj/db`

Component for describing functions to access persistence layer (in our case PostgreSQL).


### Utils

`./src/soda_clj/utils`

Namespace which contains small functions to calculate webhooks time.


### Webhooks

`./src/soda_clj/webhooks`

Component describes rules for firing webhooks.


### Migrations

`./src/soda_clj/migrations`

Database migrations

