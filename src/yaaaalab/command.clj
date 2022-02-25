(ns yaaaalab.command
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     get-namespace-mappings load-namespaces]]))

(def commands (atom []))
(defn ->commands
  []
  @commands)

(defn get-command-namespaces
  []
  (filter-namespaces (fn
                       [namespaces]
                       (filter #(re-matches #".+\.commands\..+" (str %))
                               namespaces))
                     (all-namespaces)))

(defn command?
  [mapping]
  (if (:command? (meta mapping))
    true
    false))

(defn load-command
  [command]
  (let [command-meta (meta command)]
    (swap! commands conj {:pattern (:pattern command-meta)
                          :description (:doc command-meta)
                          :group (:group command-meta)
                          :function command})))

(def get-namespace-command-mappings (partial get-namespace-mappings
                                             command?))

(defn load-commands
  []
  (reset! commands [])
  (let [loaded-command-namespaces (load-namespaces (get-command-namespaces))
        commands (flatten (map get-namespace-command-mappings
                               loaded-command-namespaces))]
    (last (map load-command commands))))

(comment
  
  (load-commands)

  )
