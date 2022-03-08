(ns yaaaalab.test.adapter
  (:require [yaaaalab.adapter :as adapter]
            [midje.sweet :refer [facts fact => with-state-changes before
                                 after provided]]))

(facts
 "about ->adapter-namespaces"
 (fact
  "it will return an empty vector when no namespaces are provided"
  (adapter/->adapter-namespaces []) => [])

 (let [namespaces ["example.adapters.1"
                   "example.test.adapters.1"
                   "example.adapters.1-test"]]
   (fact
    "it will return only 'real' adapter namespaces, and not those that may be
     test related"
    (adapter/->adapter-namespaces namespaces) => (take 1 namespaces))))

(facts
 "about adapter?"
 (fact
  "it will return false when an object does not contain meta :adapter? true"
  (adapter/adapter? {}) => false)

 (fact
  "it will return true when an object contains meta :adapter? true"
  (adapter/adapter? (with-meta [] {:adapter? true})) => true))