(ns yaaaalab.test.command
  (:require [yaaaalab.command :as command]
            [midje.sweet :refer [facts fact => with-state-changes before
                                 after provided]]))

(facts
 "about ->commands"
 (fact
  "it will return an empty vector by default, because no commands have been
   loaded"
  (command/->commands) => [])

 (let [commands [1]]
   (with-state-changes [(before :facts (reset! command/commands commands))
                        (after :facts (reset! command/commands []))]
     (fact
      "it will return whatever commands have been previously loaded"
      (command/->commands) => commands))))

(facts
 "about ->command-namespaces"
 (fact
  "it will return an empty vector when no namespaces are provided"
  (command/->command-namespaces []) => [])

 (let [namespaces ["example.commands.1"
                   "example.test.commands.1"
                   "example.commands.1-test"]]
   (fact
    "it will return only 'real' command namespaces, and not those that may be
     test related"
    (command/->command-namespaces namespaces) => (take 1 namespaces))))

(facts
 "about command?"
 (fact
  "it will return false when an object does not contain meta :command? true"
  (command/command? {}) => false)

 (fact
  "it will return true when an object contains meta :command? true"
  (command/command? (with-meta [] {:command? true})) => true))

(facts
 "about ->command-descriptions-by-group"
 (fact
  "it will return an empty hash-map when no commands have been loaded"
  (command/->command-descriptions-by-group) => {})

 (fact
  "it will return a hash-map that contains keys of every command group,
   where each key contains a collection of every related command's description;
   a related command is one that shares the same command group"
  (command/->command-descriptions-by-group) => {:test1 '(:test1 :test2)
                                                :test2 '(:test11 :test22)}
  (provided (command/->commands) => [{:group :test1 :description :test1}
                                     {:group :test1 :description :test2}
                                     {:group :test2 :description :test11}
                                     {:group :test2 :description :test22}])))

(facts
 "about load-command"
 (let [command-to-load (with-meta [] {:pattern :pattern123
                                      :doc :doc123
                                      :group :group123})]
   (with-state-changes [(before :facts (reset! command/commands []))
                        (after :facts (reset! command/commands []))]
     (fact
      "it will add the provided command to the list of available commands
       and return the entire list of available commands, which in this case
       should only be the provided commands, given our setup/teardown of state
       changes"
      (command/load-command command-to-load) => [{:description :doc123
                                                  :function []
                                                  :group :group123
                                                  :pattern :pattern123}]))))