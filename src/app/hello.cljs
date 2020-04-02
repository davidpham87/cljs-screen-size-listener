(ns app.hello
  (:require
   [goog.events :as events]
   [reagent.core :as r]))

;; Simulate the breakpoint from material design
(defn width+height->size [{:keys [width _]}]
  (cond
    (>= width 1920) :xl
    (>= width 1280) :lg
    (>= width 960) :md
    (>= width 600) :sm
    :else :xs))

(def width+height->size-deref (comp width+height->size deref))

(defonce screen-dimensions
  (r/atom {:width (.-innerWidth js/window) :height (.-innerHeight js/window)}))
(defonce screen-size (r/track! width+height->size-deref screen-dimensions))

(defn window-listeners! []
  (.addEventListener
   js/window events/EventType.RESIZE
   #(swap! screen-dimensions assoc
           :width (.-innerWidth js/window)
           :height (.-innerHeight js/window))))

(defn click-counter [click-count]
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(def click-count (r/atom 0))

(defn size->css [s]
  (->> (get {:xs 15 :sm 30 :md 40 :lg 60 :xl 80} s 20)
       (assoc-in {} [:style :font-size])))

(defn hello []
  [:<>
   [:div "This is my size " @screen-size]
   [:p (size->css @screen-size) "Hello, my-app is running!"]
   [:p (size->css @screen-size) "Here's an example of using a component with state:"]
   [click-counter click-count]])
