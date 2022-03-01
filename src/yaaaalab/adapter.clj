(ns yaaaalab.adapter
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]))

(def adapters (atom {}))

(defn ->adapter [adapter] (get @adapters adapter))

(defn ->adapter-namespaces
  []
  (filter-namespaces #".+\.adapters\..+" (all-namespaces)))

(defn adapter?
  [mapping]
  (if (:adapter? (meta mapping))
    true
    false))

(defn load-adapter
  [adapter]
  (let [adapter-meta (meta adapter)]
    (swap! adapters assoc (:id adapter-meta)
           {:function adapter})))

(def ->namespace-adapter-mappings (partial filter-namespace-mappings
                                           adapter?))

(defn load-adapters
  []
  (reset! adapters {})
  (let [loaded-adapter-namespaces (load-namespaces (->adapter-namespaces))
        adapters (flatten (map ->namespace-adapter-mappings
                               loaded-adapter-namespaces))]
    (last (map load-adapter adapters))))

(comment

  (load-adapters))
