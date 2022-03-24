(ns yaaaalab.events.event
  (:require [taoensso.timbre :refer [error]]))

(defn on-event-exception
  {:event? true
   :id :event-exception}
  [{:keys [data exception] :as _event-exception}]
  (error (str "event exception thrown: " data))
  (error exception))
