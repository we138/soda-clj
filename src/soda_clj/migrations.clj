(ns soda-clj.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(defn migrate [config]
  (repl/migrate config))

(defn rollback [config]
  (repl/rollback config))
