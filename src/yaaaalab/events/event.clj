(ns yaaaalab.events.event
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-event
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :known-event}
  [data]
  (let [event-id (:yaaaalab.event.id (meta data))]
    (debug (str "known event (" event-id ") invoked: ") data)))

(defn on-unknown-event
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :unknown-event}
  [data]
  (let [event-id (:yaaaalab.event.id (meta data))]
    (debug (str "unknown event (" event-id ") invoked: ") data)))

(defn on-event-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :event-exception}
  [{:keys [data exception] :as _event-exception}]
  (let [event-id (:yaaaalab.event.id (meta data))]
    (error (str "event (" event-id ") exception thrown: ") data)
    (error exception)))
