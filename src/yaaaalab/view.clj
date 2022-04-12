(ns yaaaalab.view
  (:require [yaaaalab.config :refer [->config]]
            [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]
            [yaaaalab.event :refer [emit]]))

(def views (atom {}))

(defn ->views
  []
  @views)

(defn ->adapter-view
  ([view-id] (->adapter-view view-id :default))
  ([view-id adapter-id]
   (keyword (str (symbol view-id) "." (symbol adapter-id)))))

(defn ->view
  [view-id]
  (let [adapter-view (->adapter-view view-id (:yaaaalab.adapter.id (->config)))
        default-view (->adapter-view view-id)]
    (cond
      (adapter-view (->views)) (adapter-view (->views))
      (default-view (->views)) (default-view (->views)))))

(defn ->view-namespaces
  [namespaces]
  (filter-namespaces #".+\.views\..+" namespaces))

(defn view?
  [mapping]
  (if (:yaaaalab.view.view? (meta mapping))
    true
    false))

(defn load-view
  [view]
  (let [view-meta (meta view)
        view-id (:yaaaalab.view.id view-meta)
        adapter-id (:yaaaalab.view.adapter.id view-meta)
        view-key (->adapter-view view-id adapter-id)]
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
    (emit :yaaaalab.event.known-view data)
    (apply-view-function data)
    (catch Exception exception
      (emit :yaaaalab.event.view-exception {:data data
                                            :exception exception}))))

(defn ->data-w-id
  [id data]
  (with-meta
    data
    (merge (meta data)
           {:yaaaalab.view.id id})))

(defn render
  [id data]
  (let [data-w-id (->data-w-id id data)]
    (if-let [view (->view id)]
      (apply-view data-w-id view)
      (emit :yaaaalab.event.unknown-view data-w-id))))

(comment

  (load-views))
