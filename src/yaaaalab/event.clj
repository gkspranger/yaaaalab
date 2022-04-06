(ns yaaaalab.event
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]))

(def events (atom []))

(defn ->events
  []
  @events)

(defn ->event-namespaces
  [namespaces]
  (filter-namespaces #".+\.events\..+" namespaces))

(defn event?
  [mapping]
  (if (:event? (meta mapping))
    true
    false))

(defn load-event
  [event]
  (swap! events conj {:id (:id (meta event))
                      :function event}))

(def ->namespace-event-mappings (partial filter-namespace-mappings
                                         event?))

(defn load-events
  []
  (reset! events [])
  (let [event-namespaces (->event-namespaces (all-namespaces))
        loaded-event-namespaces (load-namespaces event-namespaces)
        events (flatten (map ->namespace-event-mappings
                             loaded-event-namespaces))]
    (last (map load-event events))))

(declare emit)

(defn apply-event
  [data
   {apply-event-function :function :as _matched-event}]
  (try
    (apply-event-function data)
    (catch Exception exception
      (emit :event-exception {:data data
                              :exception exception}))))

(defn emit
  [id data]
  (let [matched-events (filterv #(= id (:id %)) (->events))
        emitting-known-event-id? (= id :known-event)
        scrubbed-data (dissoc data
                              :event-emitter :message-responder
                              :message-sender :view-renderer)]
    (cond
      (and matched-events emitting-known-event-id?)
      (run! #(apply-event scrubbed-data %) matched-events)
      matched-events (do (emit :known-event scrubbed-data)
                         (run! #(apply-event scrubbed-data %) matched-events))
      :else (emit :unknown-event data))))

(comment

  (load-events))
