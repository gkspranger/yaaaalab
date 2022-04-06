(ns yaaaalab.events.listener
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-listener
  {:event? true
   :id :known-listener}
  [message]
  (debug (str "known listener invoked: " message)))

(defn on-listener-exception
  {:event? true
   :id :listener-exception}
  [{:keys [exception message] :as _listener-message-exception}]
  (error (str "listener exception thrown: " message))
  (error exception))
