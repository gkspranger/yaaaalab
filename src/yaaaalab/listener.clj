(ns yaaaalab.listener
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     get-namespace-mappings load-namespaces]]))

(def listeners (atom {}))
(defn ->listener-vals [] (vals @listeners))

(defn get-listener-namespaces
  []
  (filter-namespaces (fn
                       [namespaces]
                       (filter #(re-matches #".+\.listeners\..+" (str %))
                               namespaces))
                     (all-namespaces)))

(defn listener?
  [mapping]
  (if (:listener? (meta mapping))
    true
    false))

(defn load-adapter
  [listener]
  (let [listener-meta (meta listener)
        pattern (:pattern listener-meta)]
    (swap! listeners assoc (str pattern)
           {:pattern pattern
            :function listener})))

(def get-namespace-listener-mappings (partial get-namespace-mappings
                                              listener?))

(defn load-listeners
  []
  (let [loaded-adapter-namespaces (load-namespaces (get-listener-namespaces))
        adapters (flatten (map get-namespace-listener-mappings
                               loaded-adapter-namespaces))]
    (last (map load-adapter adapters))))

(comment
  
  (load-listeners)

  )
