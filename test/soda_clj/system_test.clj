(ns soda-clj.system-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [clj-time.core :as t]
            [soda-clj.system :as system]
            [soda-clj.db :as db]))

(def test-db (ig/init system/config [:soda/db]))

(deftest load-timers-test
  (testing "timers loading"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (db/create-timer tx :url "http://foobar.com"
                                   :hours 0 :minutes 0 :seconds 30)
            current-time (t/plus (t/now) (t/seconds 60))]
        (is (= (system/load-timers tx current-time) nil))))))
