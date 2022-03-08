(ns yaaaalab.core
  (:require [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.command :refer [->commands load-commands]]
            [yaaaalab.listener :refer [->listeners load-listeners]]
            [yaaaalab.view :refer [load-views]]
            [yaaaalab.config :refer [->config]]
            [clojure.string :as string])
  (:gen-class))

(defn dispatch-handler
  [message
   {:keys [match]
    {apply-command :function} :command
    {apply-listener :function} :listener :as _handler-pattern-match}]
  (let [message-w-match (assoc message :match match)]
    (cond
      (and match apply-command) (apply-command message-w-match)
      (and match apply-listener) (apply-listener message-w-match)
      :else nil)))

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

(defn filter-handler-pattern-matches
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
    (let [matched-commands (->>
                            (filter-handler-pattern-matches
                             message
                             command-pattern-match?
                             (->commands))
                            (map #(->command-pattern-match message %)))]
      (run! #(dispatch-handler message %) matched-commands))))

(defn evaluate-message-for-listeners
  [message]
  (let [matched-listeners (->>
                           (filter-handler-pattern-matches
                            message
                            listener-pattern-match?
                            (->listeners))
                           (map #(->listener-pattern-match message %)))]
    (run! #(dispatch-handler message %) matched-listeners)))

(defn -main
  [& _args]
  (load-adapters)
  (load-commands)
  (load-listeners)
  (load-views)
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
