(ns yaaaalab.events.event
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-event
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.known-event}
  [data]
  (debug (str "known event (" (:yaaaalab.event.parent.id (meta data)) ") invoked: ")
         data))

(defn on-unknown-event
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.unknown-event}
  [data]
  (debug (str "unknown event (" (:yaaaalab.event.parent.id (meta data)) ") invoked: ")
         data))

(defn on-event-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.event-exception}
  [{data :yaaaalab.event.data
    exception :yaaaalab.event.exception :as _event-exception}]
  (error (str "event (" (:yaaaalab.event.id (meta data)) ") exception thrown: ")
         data)
  (error exception))
