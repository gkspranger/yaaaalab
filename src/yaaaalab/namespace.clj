(ns yaaaalab.namespace
  (:require [clojure.java.classpath :refer [classpath system-classpath]]
            [clojure.tools.namespace.find :refer [find-namespaces]]))

(defn all-namespaces
  []
  (concat
   (find-namespaces (system-classpath))
   (find-namespaces (classpath))))

(defn load-namespace
  [y-namespace]
  (try
    (require y-namespace)
    y-namespace
    (catch Exception _)))

(defn load-namespaces
  [namespaces]
  (reduce #(when (load-namespace %2) (conj %1 %2)) [] namespaces))

(defn get-namespace-mappings
  [filter-fn y-namespace]
  (let [mappings (vals (ns-publics y-namespace))]
    (filter filter-fn mappings)))
