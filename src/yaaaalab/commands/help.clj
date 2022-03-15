(ns yaaaalab.commands.help
  (:require [yaaaalab.command :refer [->command-descriptions-by-group]]
            [yaaaalab.view :refer [render]]))

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{reply :response-dispatcher :as _message}]
  (reply (render :help {:items (->command-descriptions-by-group)})))
