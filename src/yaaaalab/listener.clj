(ns yaaaalab.listener
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     get-namespace-mappings load-namespaces]]))

(def listeners (atom []))
(defn ->listeners
  []
  @listeners)

(defn get-listener-namespaces
  []
  (filter-namespaces #".+\.listeners\..+" (all-namespaces)))

(defn listener?
  [mapping]
  (if (:listener? (meta mapping))
    true
    false))

(defn load-listener
  [listener]
  (swap! listeners conj {:pattern (:pattern (meta listener))
                         :function listener}))

(def get-namespace-listener-mappings (partial get-namespace-mappings
                                              listener?))

(defn load-listeners
  []
  (reset! listeners [])
  (let [loaded-listener-namespaces (load-namespaces (get-listener-namespaces))
        listeners (flatten (map get-namespace-listener-mappings
                                loaded-listener-namespaces))]
    (last (map load-listener listeners))))

(comment
  
  (load-listeners)

  )
