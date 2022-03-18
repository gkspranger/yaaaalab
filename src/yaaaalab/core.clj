(ns yaaaalab.core
  (:require [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.command :refer [->commands load-commands]]
            [yaaaalab.event :refer [emit load-events]]
            [yaaaalab.listener :refer [->listeners load-listeners]]
            [yaaaalab.view :refer [load-views render]]
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

(defn dispatch-handler
  [message
   {match :match
    apply-handler-function :function :as _handler-pattern-match}]
  (apply-handler-function (assoc message :match match)))

(defn evaluate-message-for-commands
  [{:keys [text] :as message}]
  (when (re-find command-prefix-pattern text)
    (let [matched-commands (->> (->commands)
                                (map #(->command-pattern-match message %))
                                (remove #(empty? (:match %))))]
      (run! #(dispatch-handler message %) matched-commands))))

(defn evaluate-message-for-listeners
  [message]
  (let [matched-listeners (->> (->listeners)
                               (map #(->listener-pattern-match message %))
                               (remove #(empty? (:match %))))]
    (run! #(dispatch-handler message %) matched-listeners)))

(def message-handlers
  {:message-evaluator (fn [message]
                        (evaluate-message-for-commands message)
                        (evaluate-message-for-listeners message))
   :event-emitter emit
   :view-renderer render})

(defn -main
  [& _args]
  (load-adapters)
  (load-commands)
  (load-events)
  (load-listeners)
  (load-views)
  (let [adapter (:adapter (->config))
        apply-adapter-function (:function (->adapter adapter))]
    (apply-adapter-function message-handlers)))
