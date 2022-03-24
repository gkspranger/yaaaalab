(ns yaaaalab.listeners.message
  (:require [taoensso.timbre :refer [debug]]))

(defn non-empty-message
  {:listener? true
   :pattern #"(.*)"}
  [{text :text :as message}]
  (when-not (empty? text)
    (debug (str "message received: "
                (select-keys message [:channel :source :text :user])))))
