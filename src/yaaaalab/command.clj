(ns yaaaalab.command
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]))

(def commands (atom []))

(defn ->commands
  []
  @commands)

(defn ->command-descriptions-by-group
  []
  (reduce #(assoc %1 (:group %2) (sort (conj ((:group %2) %1)
                                             (:description %2))))
          {}
          (->commands)))

(defn ->command-namespaces
  [namespaces]
  (filter-namespaces #".+\.commands\..+" namespaces))

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

(def ->namespace-command-mappings (partial filter-namespace-mappings
                                           command?))

(defn load-commands
  []
  (reset! commands [])
  (let [command-namespaces (->command-namespaces (all-namespaces))
        loaded-command-namespaces (load-namespaces command-namespaces)
        commands (flatten (map ->namespace-command-mappings
                               loaded-command-namespaces))]
    (last (map load-command commands))))

(defn dispatch-command
  [message
   {match :match
    apply-command-function :function :as _command-pattern-match}]
  (when match
    (apply-command-function (assoc message :match match))))

(comment

  (load-commands))
