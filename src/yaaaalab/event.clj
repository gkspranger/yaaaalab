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
  (if (:yaaaalab.event.event? (meta mapping))
    true
    false))

(defn load-event
  [event]
  (swap! events conj {:yaaaalab.event.id (:yaaaalab.event.id (meta event))
                      :yaaaalab.event.function event}))

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
   {apply-event-function :yaaaalab.event.function :as _matched-event}]
  (try
    (apply-event-function data)
    (catch Exception exception
      (emit :event-exception {:data data
                              :exception exception}))))

(defn ->data-w-ids
  [id data]
  (let [parent-event-id (:yaaaalab.event.id (meta data))]
    (with-meta
      data
      (merge {:yaaaalab.event.id id}
             (when parent-event-id
               {:yaaaalab.event.parent.id parent-event-id})))))

(defn emit
  [id data]
  (let [matched-events (filter #(= id (:yaaaalab.event.id %)) (->events))
        emitting-known-event-id? (= id :known-event)
        emitting-unknown-event-id? (= id :unknown-event)
        data-w-ids (->data-w-ids id data)]
    (cond
      ;; when non-empty matched event's id is either :known-event or :unknown-event,
      ;; only apply event function to data, thus avoiding circular reference
      ;; in following condition that emits :known-event event with data
      (and (not-empty matched-events) (or emitting-known-event-id?
                                          emitting-unknown-event-id?))
      (run! #(apply-event data-w-ids %) matched-events)
      ;; when non-empty matched events, emit :known-event event with data
      ;; and apply event function to data
      (not-empty matched-events)
      (do (emit :known-event data-w-ids)
          (run! #(apply-event data-w-ids %) matched-events))
      ;; when empty matched events, emit :unknown-event event with data
      :else (emit :unknown-event data-w-ids))))

(comment

  (load-events))
