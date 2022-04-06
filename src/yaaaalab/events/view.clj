(ns yaaaalab.events.view
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-view
  {:event? true
   :id :known-view}
  [data]
  (debug (str "known view invoked: " data)))

(defn on-unknown-view
  {:event? true
   :id :unknown-view}
  [data]
  (debug (str "unknown view invoked: " data)))

(defn on-view-exception
  {:event? true
   :id :view-exception}
  [{:keys [data exception] :as _view-exception}]
  (error (str "view exception thrown: " data))
  (error exception))
