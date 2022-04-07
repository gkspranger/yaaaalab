(ns yaaaalab.test.events.example
  (:require [yaaaalab.event :refer [emit]]
            [taoensso.timbre :refer [debug]]))

(defn on-example-event-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :example-event-exception}
  [_data]
  (/ 1 0))

(defn on-example-event-nested
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :example-event-nested}
  [data]
  (emit :example-event-nested2 data))

(defn on-example-event-nested2
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :example-event-nested2}
  [data]
  (debug "exampled event nested2" data))
