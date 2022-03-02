(ns yaaaalab.listener
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]))

(def listeners (atom []))

(defn ->listeners
  []
  @listeners)

(defn ->listener-namespaces
  [namespaces]
  (filter-namespaces #".+\.listeners\..+" namespaces))

(defn listener?
  [mapping]
  (if (:listener? (meta mapping))
    true
    false))

(defn load-listener
  [listener]
  (swap! listeners conj {:pattern (:pattern (meta listener))
                         :function listener}))

(def ->namespace-listener-mappings (partial filter-namespace-mappings
                                            listener?))

(defn load-listeners
  []
  (reset! listeners [])
  (let [listener-namespaces (->listener-namespaces (all-namespaces))
        loaded-listener-namespaces (load-namespaces listener-namespaces)
        listeners (flatten (map ->namespace-listener-mappings
                                loaded-listener-namespaces))]
    (last (map load-listener listeners))))

(comment

  (load-listeners))
