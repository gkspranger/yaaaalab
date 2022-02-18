(ns yaaaalab.adapter
  (:require [clojure.java.classpath :as cjc]
            [clojure.tools.namespace.find :as ctnf]))

(def adapters (atom {}))
(defn get-adapter [adapter] (get @adapters adapter))

(defn all-namespaces
  []
  (concat
   (ctnf/find-namespaces (cjc/system-classpath))
   (ctnf/find-namespaces (cjc/classpath))))

(defn get-adapter-namespaces
  [namespaces]
  (->> (filter #(re-matches #".+\.adapters\..+" (str %)) namespaces)
       (remove #(re-matches #".*\.test\..*" (str %)))
       (remove #(re-matches #".*-test$" (str %)))))

(defn load-namespace
  [namespace]
  (try
    (require namespace)
    namespace
    (catch Exception _)))

(defn load-namespaces
  [namespaces]
  (reduce #(when (load-namespace %2) (conj %1 %2)) [] namespaces))

(defn adapter?
  [mapping]
  (if (:adapter? (meta mapping))
    true
    false))

(defn get-namespace-adapters
  [namespace]
  (let [mappings (vals (ns-publics namespace))]
    (filter adapter? mappings)))

(defn add-adapter
  [adapter]
  (let [adapter-meta (meta adapter)]
    (swap! adapters assoc (:name adapter-meta)
           {:description (:doc adapter-meta)
            :function adapter})))

(defn add-adapters
  []
  (let [loaded-adapter-namespaces (->> (all-namespaces)
                                       (get-adapter-namespaces)
                                       (load-namespaces))
        adapters (flatten (map get-namespace-adapters loaded-adapter-namespaces))]
    (last (map add-adapter adapters))))

(comment
  
  (->> (all-namespaces)
       (get-adapter-namespaces)
       (load-namespaces)
       first
       ns-name)
  
  (add-adapters)
  
  )