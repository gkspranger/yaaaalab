(ns yaaaalab.events.command
  (:require [taoensso.timbre :refer [debug]]))

(defn on-command
  {:event? true
   :id :on-command}
  [{match :match :as _message}]
  (debug (str "command invoked: " (if (coll? match)
                                    (first match)
                                    match))))