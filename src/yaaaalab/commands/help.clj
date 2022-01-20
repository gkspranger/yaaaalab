(ns yaaaalab.commands.help
  (:require [yaaaalab.command :refer [add]]))

(defn help
  {:group :help
   :pattern #"help"
   :description "help - list all available commands"}
  [{:keys [match] :as _chat}]
  (str "help: " match))

(defn help-find
  {:group :help
   :pattern #"help\s(.+)"
   :description "help * - list all available commands that match a pattern"}
  [{:keys [match] :as _chat}]
  (str "help *: " match))

(add #'help #'help-find)
