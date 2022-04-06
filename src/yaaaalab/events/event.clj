(ns yaaaalab.events.event
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-event
  {:event? true
   :id :known-event}
  [data]
  (debug (str "known event invoked: " data)))

(defn on-unknown-event
  {:event? true
   :id :unknown-event}
  [data]
  (debug (str "unknown event invoked: " data)))

(defn on-event-exception
  {:event? true
   :id :event-exception}
  [{:keys [data exception] :as _event-exception}]
  (error (str "event exception thrown: " data))
  (error exception))
