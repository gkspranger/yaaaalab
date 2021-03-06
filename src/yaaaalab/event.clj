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
  [{apply-event-function :yaaaalab.event.function :as _matched-event}
   data]
  (try
    (apply-event-function data)
    (catch Exception exception
      (emit :yaaaalab.event.event-exception {:yaaaalab.event.data data
                                             :yaaaalab.event.exception exception}))))

(defn ->data-w-ids
  [id data]
  (let [parent-event-id (:yaaaalab.event.id (meta data))]
    (with-meta
      data
      (merge (meta data)
             {:yaaaalab.event.id id}
             (when parent-event-id
               {:yaaaalab.event.parent.id parent-event-id})))))

(defn emit
  [id data]
  (let [matched-events (filter #(= id (:yaaaalab.event.id %)) (->events))
        emitting-known-event-id? (= id :yaaaalab.event.known-event)
        emitting-unknown-event-id? (= id :yaaaalab.event.unknown-event)
        data-w-ids (->data-w-ids id data)]
    (cond
      ;; when non-empty matched event's id is either :yaaaalab.event.known-event
      ;; or :yaaaalab.event.unknown-event, only apply event function to data,
      ;; thus avoiding circular reference in following condition that emits
      ;; :yaaaalab.event.known-event event with data
      (and (not-empty matched-events) (or emitting-known-event-id?
                                          emitting-unknown-event-id?))
      (run! #(apply-event % data-w-ids) matched-events)
      ;; when non-empty matched events, emit :yaaaalab.event.known-event event
      ;; with data and apply event function to data
      (not-empty matched-events)
      (do (emit :yaaaalab.event.known-event data-w-ids)
          (run! #(apply-event % data-w-ids) matched-events))
      ;; when empty matched events, emit :yaaaalab.event.unknown-event event with data
      :else (emit :yaaaalab.event.unknown-event data-w-ids))))

(comment

  (load-events))
