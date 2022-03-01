(ns yaaaalab.core
  (:require [yaaaalab.command :refer [->commands load-commands]]
            [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.listener :refer [->listeners load-listeners]]
            [yaaaalab.config :refer [->config]]
            [clojure.string :as string])
  (:gen-class))

(defn ->default-command-response
  [{:keys [user text]}]
  (str "I'm sorry " user ", I don't understand the command: `" text "`"))

(defn dispatch-command
  [{reply :response-dispatcher :as message}
   {:keys [match]
    {apply-command :function} :command :as _command-pattern-match}]
  (if match
    (apply-command (assoc message :match match))
    (reply (->default-command-response message))))

(defn dispatch-listener
  [message
   {:keys [match]
    {apply-listener :function} :listener :as _listener-pattern-match}]
  (when match
    (apply-listener (assoc message :match match))))

(def command-prefix-pattern (re-pattern (str "^" (:prefix (->config)))))

(defn ->command-pattern-match
  [message command]
  {:command command
   :match (re-find (:pattern command)
                   (string/replace (:text message) command-prefix-pattern ""))})

(defn ->listener-pattern-match
  [message listener]
  {:listener listener
   :match (re-find (:pattern listener) (:text message))})

(defn command-pattern-match?
  [message command]
  (if (:match (->command-pattern-match message command))
    true
    false))

(defn listener-pattern-match?
  [message listener]
  (if (:match (->listener-pattern-match message listener))
    true
    false))

(defn find-handler-pattern-matches
  [message pattern-match-predicate handlers]
  (filter #(pattern-match-predicate message %) handlers))

(defn command-message?
  [message]
  (if (re-find command-prefix-pattern (:text message))
    true
    false))

(defn evaluate-message-for-commands
  [message]
  (when (command-message? message)
    (let [commands (->> (find-handler-pattern-matches message
                                                      command-pattern-match?
                                                      (->commands))
                        (map #(->command-pattern-match message %)))]
      (if (empty? commands)
        (conj [] (dispatch-command message :empty))
        (mapv #(dispatch-command message %) commands)))))

(defn evaluate-message-for-listeners
  [message]
  (let [listeners (->> (find-handler-pattern-matches message
                                                     listener-pattern-match?
                                                     (->listeners))
                       (map #(->listener-pattern-match message %)))]
    (when-not (empty? listeners)
      (mapv #(dispatch-listener message %) listeners))))

(defn -main
  [& _args]
  (load-adapters)
  (load-commands)
  (load-listeners)
  (as-> (:adapter (->config)) v
    (:function (->adapter v))
    (v {:command-handler evaluate-message-for-commands
        :listener-handler evaluate-message-for-listeners})))

(comment

  (do (load-commands)
      (load-adapters)
      (evaluate-message-for-commands {:text "!help"
                                      :response-dispatcher (fn [msg] msg)}))

  (do (load-commands)
      (load-adapters)
      (evaluate-message-for-commands {:text "!example"
                                      :response-dispatcher (fn [msg] msg)
                                      :message-dispatcher (fn [_ msg] msg)}))

  (do (load-commands)
      (load-adapters)
      (evaluate-message-for-commands {:text "!fail"
                                      :response-dispatcher (fn [msg] msg)}))

  (do (load-commands)
      (evaluate-message-for-commands {:text "some random text"}))

  (do (load-listeners)
      (evaluate-message-for-listeners {:text "some random text"}))

  (do (load-listeners)
      (evaluate-message-for-listeners {:text "!some command"}))

  (->listeners))
