(ns yaaaalab.events.example)

(defn on-example-event-exception
  {:event? true
   :id :example-event-exception}
  [_data]
  (/ 1 0))
