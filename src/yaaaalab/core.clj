(ns yaaaalab.core
  (:require [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.command :refer [load-commands]]
            [yaaaalab.event :refer [emit load-events]]
            [yaaaalab.listener :refer [load-listeners]]
            [yaaaalab.handler :refer [evaluate-message-for-commands
                                      evaluate-message-for-listeners]]
            [yaaaalab.view :refer [load-views render]]
            [yaaaalab.config :refer [->config]])
  (:gen-class))

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
