(ns yaaaalab.command
  (:require [yaaaalab.namespace :as y-namespace]))

(def commands (atom {}))
(defn ->sorted-command-keys [] (sort (keys @commands)))
(defn get-command [command] (get @commands command))

(defn get-command-namespaces
  [namespaces]
  (->> (filter #(re-matches #".+\.commands\..+" (str %)) namespaces)
       (remove #(re-matches #".*\.test\..*" (str %)))
       (remove #(re-matches #".*-test$" (str %)))))

(defn command?
  [mapping]
  (if (:command? (meta mapping))
    true
    false))

(defn load-command
  [command]
  (let [command-meta (meta command)
        pattern (:pattern command-meta)]
    (swap! commands assoc (str pattern)
           {:pattern pattern
            :description (:doc command-meta)
            :group (:group command-meta)
            :function command})))

(def get-namespace-command-mappings (partial y-namespace/get-namespace-mappings
                                             command?))

(defn load-commands
  []
  (let [loaded-command-namespaces (->> (y-namespace/all-namespaces)
                                       (get-command-namespaces)
                                       (y-namespace/load-namespaces))
        commands (flatten (map get-namespace-command-mappings
                               loaded-command-namespaces))]
    (last (map load-command commands))))

(comment
  
  (load-commands)

  )
