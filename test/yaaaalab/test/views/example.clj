(ns yaaaalab.test.views.example)

(defn example-exception
  {:view? true
   :id :example-exception
   :adapter :default}
  [_data]
  (/ 1 0))
