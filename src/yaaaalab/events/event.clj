(ns yaaaalab.events.event
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-event
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :known-event}
  [data]
  (debug (str "known event (" (:yaaaalab.event.id (meta data)) ") invoked: ")
         data))

(defn on-unknown-event
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :unknown-event}
  [data]
  (debug (str "unknown event (" (:yaaaalab.event.id (meta data)) ") invoked: ")
         data))

(defn on-event-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :event-exception}
  [{:keys [data exception] :as _event-exception}]
  (error (str "event (" (:yaaaalab.event.id (meta data)) ") exception thrown: ")
         data)
  (error exception))
