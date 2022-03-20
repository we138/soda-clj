(ns soda-clj.db-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [soda-clj.db :refer :all]
            [soda-clj.system :as system]))

(def test-db (ig/init system/config [:soda/db]))

(deftest complete-timer-test
  (testing "timer complete"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (create-timer tx :url "http://foobar.com" :hours 0 :minutes 0 :seconds 1)
            id (:timers/id timer)
            completed-at (c/to-sql-time (t/now))]
        (is (= (:timers/completed_at (complete-timer tx :id id :completed-at completed-at)) completed-at))))))

(deftest create-timer-test
  (testing "timer create"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (create-timer tx :url "http://foobar.com" :hours 0 :minutes 0 :seconds 1)]

        (is (int? (:timers/id timer)))
        (is (= (:timers/url timer) "http://foobar.com"))
        (is (= (:timers/hours timer) 0))
        (is (= (:timers/minutes timer) 0))
        (is (= (:timers/seconds timer) 1))))))

(deftest fetch-timer-test
  (testing "timer fetch"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (create-timer tx :url "http://foobar.com" :hours 0 :minutes 0 :seconds 1)
            id (:timers/id timer)]

        (is (= (:timers/id (fetch-timer tx :id id)) id))))))


