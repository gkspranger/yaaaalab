(ns yaaaalab.events.listener
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-listener
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.known-listener}
  [message]
  (debug (str "known listener invoked: " message)))

(defn on-no-matching-listeners
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.no-matching-listeners}
  [message]
  (debug (str "no matching listeners found: " message)))

(defn on-listener-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.listener-exception}
  [{:keys [exception message] :as _listener-message-exception}]
  (error (str "listener exception thrown: " message))
  (error exception))
