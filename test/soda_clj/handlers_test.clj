(ns soda-clj.handlers-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [soda-clj.handlers :refer :all]
            [soda-clj.system :as system]
            [soda-clj.db :as db]))

(def test-db (ig/init system/config [:soda/db]))

(deftest create-timer-test
  (testing "timer creation"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (testing "valid input"
        (let [handler (create-timer tx)
              input {:parameters {:body {:url "http://foobar.com" :hours 1
                                         :minutes 30 :seconds 30}}}]
          (is (= (:status (handler input)) 200))
          (is (int? (get-in (handler input) [:body :id])))))

      (testing "invalid input"
        (let [handler (create-timer tx)
              input {:parameters {:body {:url "http://foobar.com" :hours 1
                                         :minutes 3000 :seconds 30}}}]
          (is (= (:status (handler input)) 422))
          (is (= (get-in (handler input) [:body :errors :minutes]) ["should be between 0 and 60"])))))))

(deftest fetch-timer-test
  (testing "timer fetching"
    (next.jdbc/with-transaction [tx (:soda/db test-db) {:rollback-only true}]
      (let [timer (db/create-timer tx :url "http://foobar.com" :hours 10 :minutes 20 :seconds 30)]
        (testing "valid input"
          (let [handler (fetch-timer tx)
                input {:parameters {:path {:id (:timers/id timer)}}}]
            (is (= (:status (handler input)) 200))
            (is (int? (get-in (handler input) [:body :id])))
            (is (= (get-in (handler input) [:body :id]) (:timers/id timer)))))

        (testing "invalid input"
          (let [handler (fetch-timer tx)
                input {:parameters {:path {:id 0}}}]
            (is (= (:status (handler input)) 404))
            (is (= (get-in (handler input) [:body :errors :id]) ["timer with this id didn't find"]))))))))
