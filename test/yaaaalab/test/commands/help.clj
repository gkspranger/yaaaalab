(ns yaaaalab.test.commands.help
  (:require [midje.sweet :refer [facts fact =>]]
            [yaaaalab.commands.help :refer [help]]
            [yaaaalab.core :as core]))

(facts
 "about .."
 (fact
  "it will .."
  (help {:match "i am a match"}) => "help: i am a match"))
