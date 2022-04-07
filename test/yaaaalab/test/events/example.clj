(ns yaaaalab.test.events.example)

(defn on-example-event-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :example-event-exception}
  [_data]
  (/ 1 0))
