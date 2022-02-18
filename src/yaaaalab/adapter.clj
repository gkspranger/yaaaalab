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

(defn add-adapter
  [adapter]
  (let [adapter-meta (meta adapter)]
    (swap! adapters assoc (:moniker adapter-meta)
           {:function adapter})))

(def get-namespace-adapters (partial y-namespace/get-namespace-resources
                                     adapter?))

(defn add-adapters
  []
  (let [loaded-adapter-namespaces (->> (y-namespace/all-namespaces)
                                       (get-adapter-namespaces)
                                       (y-namespace/load-namespaces))
        adapters (flatten (map get-namespace-adapters loaded-adapter-namespaces))]
    (last (map add-adapter adapters))))

(comment
  
  (add-adapters)

  )
