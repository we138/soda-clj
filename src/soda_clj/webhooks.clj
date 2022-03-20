(ns soda-clj.webhooks
  (:require [soda-clj.db :as db]
            [soda-clj.http-client :as http-client]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [cambium.core :as log]))

(defn fire [db & {:keys [url id] :as opts}]
  (http-client/make-request url id)
  (log/info {:args opts} "webhook fired")
  (db/complete-timer db :id id :completed-at (c/to-sql-time (t/now))))

(defn create [db & {:keys [url seconds id] :as opts}]
  (log/info {:args opts} "webhook created")

  (future
    (Thread/sleep (* seconds 1000))
    (fire db :id id :url url)))
