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

(defn dispatch-event
  [data
   {apply-event :function :as _event}]
  (apply-event data))

(defn emit
  [id data]
  (let [matched-events (filter #(= id (:id %)) (->events))]
    (run! #(dispatch-event data %) matched-events)))

(comment

  (load-events)

  (emit :nothing {:match "hello"})

  (emit :on-command {:match "hello"}))
