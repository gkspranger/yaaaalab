(ns yaaaalab.command
  (:require [yaaaalab.event :refer [emit]]
            [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]
            [yaaaalab.config :refer [->config]]
            [clojure.string :as string]))

(def commands (atom []))

(defn ->commands
  []
  @commands)

(defn ->command-namespaces
  [namespaces]
  (filter-namespaces #".+\.commands\..+" namespaces))

(defn command?
  [mapping]
  (if (:yaaaalab.command.command? (meta mapping))
    true
    false))

(defn load-command
  [command]
  (let [{pattern :yaaaalab.command.pattern
         description :doc
         group :yaaaalab.command.group
         id :yaaaalab.command.id} (meta command)]
    (swap! commands conj {:yaaaalab.command.pattern pattern
                          :yaaaalab.command.description description
                          :yaaaalab.command.group group
                          :yaaaalab.command.id id
                          :yaaaalab.command.function command})))

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

(defn command-prefix-pattern
  []
  (re-pattern (str "^" (:yaaaalab.bot.prefix (->config)))))

(defn ->command-message-pattern-match
  [{:keys [text] :as _message}
   {pattern :yaaaalab.command.pattern :as command}]
  (when-let [match (re-find pattern
                            (string/replace text (command-prefix-pattern) ""))]
    (assoc command :match match)))

(defn filter-matched-commands
  [{:keys [text] :as message}]
  (when (re-find (command-prefix-pattern) text)
    (->> (->commands)
         (map #(->command-message-pattern-match message %))
         (remove empty?))))

(defn ->message-w-id
  [id message]
  (with-meta
    message
    (merge (meta message)
           {:yaaaalab.command.id id})))

(defn apply-command
  [message
   {match :match
    command-id :yaaaalab.command.id
    apply-command-function :yaaaalab.command.function :as _matched-command}]
  (let [message-w-match (assoc message :match match)
        message-w-id (->message-w-id command-id message-w-match)]
    (try
      (emit :yaaaalab.event.known-command message-w-id)
      (apply-command-function message-w-id)
      (catch Exception exception
        (emit :yaaaalab.event.command-exception {:message message-w-id
                                                 :exception exception})))))

(defn evaluate-message-and-apply-matched-commands
  [message]
  (let [matched-commands (filter-matched-commands message)
        unknown-command? (and (coll? matched-commands)
                              (empty? matched-commands))]
    (if unknown-command?
      (emit :yaaaalab.event.unknown-command message)
      (run! #(apply-command message %) matched-commands))))

(comment

  (load-commands))
