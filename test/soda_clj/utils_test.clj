(ns soda-clj.utils-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [clj-time.core :as t]
            [soda-clj.utils :refer :all]
            [soda-clj.system :as system]
            [soda-clj.db :as db]))

(def test-db (ig/init system/config [:soda/db]))

(deftest calculate-seconds-test
  (testing "seconds calculating"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (db/create-timer tx :url "http://foobar.com"
                                   :hours 1 :minutes 30 :seconds 30)]
        (is (= (calculate-seconds timer) (+ (* 1 60 60) (* 30 60) 30)))))))

(deftest calculate-seconds-left-test
  (testing "left seconds calculating"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (db/create-timer tx :url "http://foobar.com"
                                   :hours 1 :minutes 30 :seconds 30)
            current-time (t/plus (t/now) (t/seconds 30))]
        (is (>= (calculate-seconds-left timer current-time) (+ (* 1 60 60) (* 30 60))))))))
