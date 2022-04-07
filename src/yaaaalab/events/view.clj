(ns yaaaalab.events.view
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-view
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :known-view}
  [data]
  (debug (str "known view invoked: " data)))

(defn on-unknown-view
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :unknown-view}
  [data]
  (debug (str "unknown view invoked: " data)))

(defn on-view-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :view-exception}
  [{:keys [data exception] :as _view-exception}]
  (error (str "view exception thrown: " data))
  (error exception))
