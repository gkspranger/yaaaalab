(ns yaaaalab.namespace
  (:require [clojure.java.classpath :refer [classpath system-classpath]]
            [clojure.tools.namespace.find :refer [find-namespaces]]
            [yaaaalab.config :refer [->config]]))

(defn all-namespaces
  []
  (concat
   (find-namespaces (system-classpath))
   (find-namespaces (classpath))))

(defn filter-namespaces
  [filter-pattern namespaces]
  (let [matched-namespaces (filter #(re-matches filter-pattern (str %))
                                   namespaces)
        matched-namespaces-wo-tests (->> matched-namespaces
                                         (remove #(re-matches #".*\.test\..*" (str %)))
                                         (remove #(re-matches #".*-test$" (str %))))]
    (if (:yaaaalab.examples.include? (->config))
      matched-namespaces
      matched-namespaces-wo-tests)))

(defn load-namespace
  [y-namespace]
  (try
    (require y-namespace)
    y-namespace
    (catch Exception _)))

(defn load-namespaces
  [namespaces]
  (reduce #(when (load-namespace %2) (conj %1 %2)) [] namespaces))

(defn filter-namespace-mappings
  [filter-fn y-namespace]
  (filter filter-fn (vals (ns-publics y-namespace))))
