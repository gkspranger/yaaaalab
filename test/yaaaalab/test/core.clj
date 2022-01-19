(ns yaaaalab.test.core
  (:require [midje.sweet :refer [facts fact => contains]]
            [yaaaalab.commands.help]
            [yaaaalab.core :as core]))

(facts
 "about .."
 (fact
  "it will .."
  (core/default-response {:message "m123"
                          :user "u123"}) => #"(?is)sorry.*u123.*understand.*m123"))
