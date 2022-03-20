(ns soda-clj.router
  (:require
   [soda-clj.handlers :as handlers]
   [muuntaja.core :as m]
   [reitit.ring :as ring]
   [reitit.ring.coercion :as rrc]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.coercion.malli]))

(defn config []
  {:data {:muuntaja m/instance
          :middleware [parameters/parameters-middleware
                       muuntaja/format-middleware
                       rrc/coerce-exceptions-middleware
                       rrc/coerce-request-middleware
                       rrc/coerce-response-middleware]}})

(defn routes [db]
  (ring/ring-handler
   (ring/router
    [["/timers" {:post {:handler (handlers/create-timer db)}
                 :coercion reitit.coercion.malli/coercion
                 :parameters {:body {:url string? :hours int?
                                     :minutes int? :seconds int?}}}]
     ["/timers/:id" {:get {:handler (handlers/fetch-timer db)}
                     :coercion reitit.coercion.malli/coercion
                     :parameters {:path {:id pos-int?}}}]]
    (config))))
