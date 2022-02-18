(ns yaaaalab.adapter
  (:require [yaaaalab.namespace :refer [all-namespaces get-namespace-mappings
                                        load-namespaces]]))

(def adapters (atom {}))
(defn get-adapter [adapter] (get @adapters adapter))

(defn get-adapter-namespaces
  [namespaces]
  (->> (filter #(re-matches #".+\.adapters\..+" (str %)) namespaces)
       (remove #(re-matches #".*\.test\..*" (str %)))
       (remove #(re-matches #".*-test$" (str %)))))

(defn adapter?
  [mapping]
  (if (:adapter? (meta mapping))
    true
    false))

(defn load-adapter
  [adapter]
  (let [adapter-meta (meta adapter)]
    (swap! adapters assoc (:moniker adapter-meta)
           {:function adapter})))

(def get-namespace-adapter-mappings (partial get-namespace-mappings
                                             adapter?))

(defn load-adapters
  []
  (let [loaded-adapter-namespaces (->> (all-namespaces)
                                       (get-adapter-namespaces)
                                       (load-namespaces))
        adapters (flatten (map get-namespace-adapter-mappings
                               loaded-adapter-namespaces))]
    (last (map load-adapter adapters))))

(comment
  
  (load-adapters)

  )
