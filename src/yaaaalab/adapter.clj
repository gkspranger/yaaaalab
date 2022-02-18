(ns yaaaalab.adapter
  (:require [yaaaalab.namespace :as y-namespace]))

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

(def get-namespace-adapter-mappings (partial y-namespace/get-namespace-mappings
                                             adapter?))

(defn load-adapters
  []
  (let [loaded-adapter-namespaces (->> (y-namespace/all-namespaces)
                                       (get-adapter-namespaces)
                                       (y-namespace/load-namespaces))
        adapters (flatten (map get-namespace-adapter-mappings
                               loaded-adapter-namespaces))]
    (last (map load-adapter adapters))))

(comment
  
  (load-adapters)

  )
