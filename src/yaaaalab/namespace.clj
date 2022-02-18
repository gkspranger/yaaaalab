(ns yaaaalab.namespace
  (:require [clojure.java.classpath :as cjc]
            [clojure.tools.namespace.find :as ctnf]))

(defn all-namespaces
  []
  (concat
   (ctnf/find-namespaces (cjc/system-classpath))
   (ctnf/find-namespaces (cjc/classpath))))

(defn load-namespace
  [y-namespace]
  (try
    (require y-namespace)
    y-namespace
    (catch Exception _)))

(defn load-namespaces
  [namespaces]
  (reduce #(when (load-namespace %2) (conj %1 %2)) [] namespaces))

(defn get-namespace-resources
  [y-namespace filter-fn]
  (let [mappings (vals (ns-publics y-namespace))]
    (filter filter-fn mappings)))
