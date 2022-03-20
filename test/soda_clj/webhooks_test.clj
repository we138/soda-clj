(ns soda-clj.webhooks-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [soda-clj.webhooks :refer :all]
            [soda-clj.system :as system]
            [soda-clj.db :as db]))

(def test-db (ig/init system/config [:soda/db]))

(deftest create-test
  (testing "webhook creation"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (db/create-timer tx :url "http://foobar.com"
                                   :hours 0 :minutes 0 :seconds 1)
            id (:timers/id timer)
            webhook (create tx :id id :url (:timers/url timer) :seconds 0)]
        (is (-> @webhook :timers/completed_at some?))))))
