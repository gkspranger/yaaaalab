(ns yaaaalab.events.exception
  (:require [taoensso.timbre :refer [error]]))

(defn on-handler-exception
  {:event? true
   :id :on-handler-exception}
  [{exception :exception :as _message-w-exception}]
  (error exception))
