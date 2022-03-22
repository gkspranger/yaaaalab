(ns yaaaalab.command
  (:require [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]
            [yaaaalab.config :refer [->config]]
            [clojure.string :as string]))

(def commands (atom []))

(defn ->commands
  []
  @commands)

(defn ->command-descriptions-by-group
  []
  (reduce #(let [group-name (name (:group %2))
                 command-description (str (:prefix (->config))
                                          (:description %2))]
             (assoc %1 group-name (sort (conj (%1 group-name)
                                              command-description))))
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

(def command-prefix-pattern (re-pattern (str "^" (:prefix (->config)))))

(defn ->command-pattern-match
  [{:keys [text] :as _message}
   {:keys [pattern] :as command}]
  (when-let [match (re-find pattern
                            (string/replace text command-prefix-pattern ""))]
    (assoc command :match match)))

(defn filter-matched-commands
  [{:keys [text] :as message}]
  (when (re-find command-prefix-pattern text)
    (->> (->commands)
         (map #(->command-pattern-match message %))
         (remove empty?))))

(comment

  (load-commands))
