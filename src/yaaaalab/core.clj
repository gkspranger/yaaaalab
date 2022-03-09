(ns yaaaalab.core
  (:require [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.command :refer [->commands load-commands]]
            [yaaaalab.listener :refer [->listeners load-listeners]]
            [yaaaalab.view :refer [load-views]]
            [yaaaalab.config :refer [->config]]
            [clojure.string :as string])
  (:gen-class))

(def command-prefix-pattern (re-pattern (str "^" (:prefix (->config)))))

(defn ->command-pattern-match
  [{:keys [text] :as _message}
   {:keys [pattern] :as command}]
  (assoc command :match (re-find pattern
                                 (string/replace text command-prefix-pattern ""))))

(defn ->listener-pattern-match
  [{:keys [text] :as _message}
   {:keys [pattern] :as listener}]
  (assoc listener :match (re-find pattern text)))

(defn command-message?
  [message]
  (if (re-find command-prefix-pattern (:text message))
    true
    false))

(defn dispatch-handler
  [message
   {match :match
    apply-handler-function :function :as _handler-pattern-match}]
  (apply-handler-function (assoc message :match match)))

(defn evaluate-message-for-commands
  [message]
  (when (command-message? message)
    (let [matched-commands (->>
                            (->commands)
                            (map #(->command-pattern-match message %))
                            (remove #(empty? (:match %))))]
      (run! #(dispatch-handler message %) matched-commands))))

(defn evaluate-message-for-listeners
  [message]
  (let [matched-listeners (->>
                           (->listeners)
                           (map #(->listener-pattern-match message %))
                           (remove #(empty? (:match %))))]
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
