(ns soda-clj.utils
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]))

(defn calculate-seconds [timer]
  (+ (-> timer :timers/hours (* 60 60))
     (-> timer :timers/minutes (* 60))
     (-> timer :timers/seconds)))

(defn calculate-seconds-left [timer from-time]
  (let [created_at (c/from-sql-time (:timers/created_at timer))
        seconds (calculate-seconds timer)
        fire-at (t/plus created_at (t/seconds seconds))]
    (t/in-seconds (t/interval from-time fire-at))))
