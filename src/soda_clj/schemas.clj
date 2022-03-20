(ns soda-clj.schemas
  (:require [malli.core :as m]
            [malli.error :as me]))

(def CreateTimer
  [:map
   [:url :string]
   [:hours [:int {:min 0}]]
   [:minutes [:int {:min 0 :max 60}]]
   [:seconds [:int {:min 0 :max 60}]]])
