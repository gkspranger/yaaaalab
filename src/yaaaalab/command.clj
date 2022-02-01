(ns yaaaalab.command
  (:require [clojure.java.classpath :as cjc]
            [clojure.tools.namespace.find :as ctnf]))

(def commands (atom {}))
(defn sorted-keys [] (sort (keys @commands)))
(defn fetch [command] (get @commands command))

(defn add-command
  [command]
  (let [command-meta (meta command)
        pattern (:pattern command-meta)]
    (swap! commands assoc (str pattern)
           {:pattern pattern
            :description (:doc command-meta)
            :group (:group command-meta)
            :function command})))

(defn all-namespaces
  []
  (concat
   (ctnf/find-namespaces (cjc/system-classpath))
   (ctnf/find-namespaces (cjc/classpath))))

(defn command-namespaces
  [namespaces]
  (->> (filter #(re-matches #".+\.commands\..+" (str %)) namespaces)
       (remove #(re-matches #".*\.test\..*" (str %)))))

(defn load-namespace
  [namespace]
  (try
    (require namespace)
    namespace
    (catch Exception _)))

(defn load-namespaces
  [namespaces]
  (reduce #(when (load-namespace %2) (conj %1 %2)) [] namespaces))

(defn command?
  [mapping]
  (if (:command? (meta mapping))
    true
    false))

(defn namespace-commands
  [namespace]
  (let [mappings (vals (ns-publics namespace))]
    (filter command? mappings)))

(defn add-commands
  []
  (let [loaded-command-namespaces (->> (all-namespaces)
                                       (command-namespaces)
                                       (load-namespaces))
        commands (flatten (map namespace-commands loaded-command-namespaces))]
    (last (map add-command commands))))

(comment
  
  (add-commands)

  )