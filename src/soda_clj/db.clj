(ns soda-clj.db
  (:require [next.jdbc.sql :as sql]
            [next.jdbc :as jdbc]))

(defn create-timer [db & {:keys [url hours minutes seconds] :as opts}]
  (sql/insert! db :timers opts))

(defn complete-timer [db & {:keys [id completed-at]}]
  (jdbc/execute-one! db ["update timers set completed_at = ? where id = ? returning *" completed-at id]))

(defn fetch-timer [db & {:keys [id]}]
  (sql/get-by-id db :timers id))

(defn find-uncompleted-timers [db]
  (sql/query db ["select * from timers where completed_at IS NULL"]))
