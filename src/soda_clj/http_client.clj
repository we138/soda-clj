(ns soda-clj.http-client
  (:require [soda-clj.db :as db]
            [clj-http.client :as client]))

(defn make-request [url id]
  (try (client/post (str url "/" id))
       (catch clojure.lang.ExceptionInfo _)))
