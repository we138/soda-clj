(ns soda-clj.system
  (:require [soda-clj.router :as router]
            [soda-clj.migrations :as migrations]
            [soda-clj.webhooks :as webhooks]
            [soda-clj.utils :as utils]
            [soda-clj.db :as db]
            [integrant.core :as ig]
            [ring.adapter.jetty :as server]
            [clojure.java.io :as io]
            [next.jdbc :as jdbc]
            [ragtime.jdbc :as rjdbc]
            [clj-time.core :as t]
            [cambium.core :as log]))

(def config
  {:soda/app {:db (ig/ref :soda/db)}
   :soda/db-config {}
   :soda/migrations {:db-config (ig/ref :soda/db-config)}
   :soda/server {:handler (ig/ref :soda/app) :port 3000}
   :soda/db {:db-config (ig/ref :soda/db-config)}})

(defn app [db]
  (router/routes db))

(defn load-timers [db current-time]
  (doseq [timer (db/find-uncompleted-timers db)]
    (try (webhooks/create db :url (:timers/url timer) :seconds (utils/calculate-seconds-left timer current-time) :id (:timers/id timer))
         (catch java.lang.IllegalArgumentException _
           (webhooks/fire db :id (:timers/id timer) :url (:timers/url timer))))))

(defn run-migrations []
  (migrations/migrate (:soda/migrations (ig/init config [:soda/migrations]))))

(defmethod ig/init-key :soda/app [_ {:keys [db]}]
  (log/info "Application started")
  (load-timers db (t/now))
  (app db))

(defmethod ig/init-key :soda/migrations [_ {:keys [db-config]}]
  {:datastore (rjdbc/sql-database db-config)
   :migrations (rjdbc/load-resources "migrations")})

(defmethod ig/init-key :soda/server [_ {:keys [handler port]}]
  (server/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :soda/db [_ {:keys [db-config]}]
  (jdbc/get-datasource db-config))

(defmethod ig/init-key :soda/db-config [_ _]
  {:dbtype "postgresql"
   :dbname (System/getenv "DB_NAME")
   :host (System/getenv "DB_HOST")
   :user (System/getenv "DB_USER")
   :password (System/getenv "DB_PASSWORD")})

(defmethod ig/halt-key! :soda/server [_ server]
  (.stop server))

(defn -main []
  (ig/init config))
