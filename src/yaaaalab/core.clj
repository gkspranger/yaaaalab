(ns yaaaalab.core
  (:require [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.command :refer [filter-matched-commands load-commands]]
            [yaaaalab.event :refer [emit load-events]]
            [yaaaalab.listener :refer [->listeners load-listeners]]
            [yaaaalab.view :refer [load-views render]]
            [yaaaalab.config :refer [->config]])
  (:gen-class))

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
  [message]
  (run! #(dispatch-handler message %) (filter-matched-commands message)))

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
