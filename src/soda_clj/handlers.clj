(ns soda-clj.handlers
  (:require [soda-clj.webhooks :as webhooks]
            [soda-clj.db :as db]
            [soda-clj.utils :as utils]
            [soda-clj.schemas :as schemas]
            [clj-time.core :as t]
            [malli.core :as m]
            [malli.error :as me]
            [cambium.core :as log]))

(defn create-timer [db]
  (fn [{{:keys [body]} :parameters}]
    (log/info {:args body} "create timer")

    (if (m/validate schemas/CreateTimer body)
      (let [url (:url body)
            hours (:hours body)
            minutes (:minutes body)
            seconds (:seconds body)
            timer (db/create-timer db :url url :hours hours :minutes minutes :seconds seconds)]

        (webhooks/create db :url url :seconds (utils/calculate-seconds timer) :id (:timers/id timer))

        {:status 200 :body {:id (:timers/id timer)}})
      {:status 422 :body {:errors (-> schemas/CreateTimer (m/explain body) (me/humanize))}})))

(defn fetch-timer [db]
  (fn [{{:keys [path]} :parameters}]
    (let [id (:id path)
          timer (db/fetch-timer db :id id)]
      (log/info {:argc id} "fetch timer")
      (if (some? timer)
        {:status 200 :body {:id id
                            :time_left (try (utils/calculate-seconds-left timer (t/now))
                                            (catch java.lang.IllegalArgumentException _ 0))}}
        {:status 404 :body {:errors {:id ["timer with this id didn't find"]}}}))))
