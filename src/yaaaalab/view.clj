(ns yaaaalab.view
  (:require [yaaaalab.config :refer [->config]]
            [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]
            [yaaaalab.event :refer [emit]]))

(def views (atom {}))

(defn ->view
  [view]
  (let [adapter-view (keyword (str (symbol view) "/" (symbol (:adapter (->config)))))
        default-view (keyword (str (symbol view) "/default"))]
    (cond
      (adapter-view @views) (adapter-view @views)
      (default-view @views) (default-view @views)
      :else (fn [& _args] nil))))

(defn ->view-namespaces
  [namespaces]
  (filter-namespaces #".+\.views\..+" namespaces))

(defn view?
  [mapping]
  (if (:view? (meta mapping))
    true
    false))

(defn load-view
  [view]
  (let [view-meta (meta view)
        view-key (keyword (str (symbol (:id view-meta))
                               "/"
                               (symbol (:adapter view-meta))))]
    (swap! views assoc view-key view)))

(def ->namespace-view-mappings (partial filter-namespace-mappings
                                        view?))

(defn load-views
  []
  (reset! views {})
  (let [view-namespaces (->view-namespaces (all-namespaces))
        loaded-view-namespaces (load-namespaces view-namespaces)
        views (flatten (map ->namespace-view-mappings
                            loaded-view-namespaces))]
    (last (map load-view views))))

(defn apply-view
  [data apply-view-function]
  (try
    (apply-view-function data)
    (catch Exception exception
      (emit :view-exception {:data data
                             :exception exception}))))

(defn render
  [id data]
  (apply-view data (->view id)))

(comment

  (load-views))
