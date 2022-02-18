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

(defn add-command
  [command]
  (let [command-meta (meta command)
        pattern (:pattern command-meta)]
    (swap! commands assoc (str pattern)
           {:pattern pattern
            :description (:doc command-meta)
            :group (:group command-meta)
            :function command})))

(def get-namespace-commands (partial y-namespace/get-namespace-resources
                                     command?))

(defn add-commands
  []
  (let [loaded-command-namespaces (->> (y-namespace/all-namespaces)
                                       (get-command-namespaces)
                                       (y-namespace/load-namespaces))
        commands (flatten (map get-namespace-commands loaded-command-namespaces))]
    (last (map add-command commands))))

(comment
  
  (add-commands)

  )
