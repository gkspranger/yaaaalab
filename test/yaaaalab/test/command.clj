(ns yaaaalab.test.command
  (:require [midje.sweet :refer [facts fact => contains]]
            [yaaaalab.command :as command]))

(defn test1
  {:group :test
   :pattern #"test1"
   :description "this is test command 1"}
  [_]
  "test1")

(defn test2
  {:group :test
   :pattern #"test2"
   :description "this is test command 2"}
  [_]
  "test2")

(facts
 "about add-command"
 (fact
  "it will add the supplied argument to the collection of available commands,
   and return the entirety of available commands"
  (let [test1-meta (select-keys (meta #'test1) [:group :pattern :description])]
    (get (command/add-command #'test1) (str (:pattern test1-meta))) =>
    (contains test1-meta))))
