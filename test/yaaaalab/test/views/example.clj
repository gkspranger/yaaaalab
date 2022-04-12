(ns yaaaalab.test.views.example)

(defn example-exception
  {:yaaaalab.view.view? true
   :yaaaalab.view.id :example-exception
   :yaaaalab.view.adapter.id :default}
  [_data]
  (/ 1 0))
