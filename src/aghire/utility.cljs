(ns aghire.utility
  (:require
    [goog.string :as gs]
    [reagent.core :as r]
    [clojure.string :as str]
    [aghire.db :as db]
    [cljs.reader :as reader]))

(defn app-cursor [path]
  (r/cursor db/app (if (vector? path) path [path])))

(defn <app-cursor [path]
  @(app-cursor path))

(defn <track [fn & path]
  @(apply r/track fn path))



(defn slide-in-anime [show?]
  (if show? "slideIn" "slideOut"))

(defn help-list [helpItems helping]
  (fn []
    [:div {:class    (str "help " (slide-in-anime @helping))
           :style    {:display     (if @helping "block" "none")
                      :margin-top  0
                      :padding-top "6px"}
           :on-click (fn [mx]
                       (reset! helping false))}
     [:div {:style {:cursor      "pointer"
                    :textAlign   "left"
                    :marginRight "18px"}}
      (into [:ul {:style {:listStyle  "none"
                          :marginLeft 0}}]
        (map (fn [e]
               [:li {
                     :style {:padding 0
                             :margin  "0 18px 9px 0"}}
                [:div {:dangerouslySetInnerHTML {:__html e}}]])
          helpItems))]]))


;;; --- local-store --------------------------------

(defn io-all-keys []
  (.keys js/Object (.-localStorage js/window)))

(defn ls-get-wild
  "Loads all localStorage values whose key begins with
  prefix into a dictionary, using the rest of the LS key
   as the dictionary key."
  [prefix]

  (into {}
    (remove nil?
      (for [lsk (io-all-keys)]
        (when (and (str/starts-with? lsk prefix)
                   ;; ugh, we got some garbage in LS
                   ;; may as well create permanent filter
                   (> (count lsk) (count prefix)))
          [(subs lsk (count prefix))                        ;; toss prefix
           (reader/read-string
             (.getItem js/localStorage lsk))])))))

(defn target-val [e]
  (.-value (.-target e)))

;;; --- handy CSS --------------------------------------------

(def hz-flex-wrap {:display   "flex"
                   :flex-wrap "wrap"})

(def hz-flex-wrap-centered
  {:display     "flex"
   :flex-wrap   "wrap"
   :align-items "center"})

(def hz-flex-wrap-bottom
  {:display     "flex"
   :flex-wrap   "wrap"
   :align-items "bottom"})

(defn unesc [entity]
  (gs/unescapeEntities entity))

;;; --- sorting ---------------------------------------------

(defn job-company-key [j]
  (or (:company j) ""))

;(defn job-stars-enrich [job]
;  (assoc job :stars (or (<sub [:memo-prop (:hn-id job) :stars]) 0)))

(defn job-stars-compare [dir j k]
  ;; force un-starred to end regardless of sort order
  ;; order ties by ascending hn-id

  (let [j-stars (:stars j)
        k-stars (:stars k)]

    (let [r (if (pos? j-stars)
              (if (pos? k-stars)
                (* dir (if (< j-stars k-stars)
                         -1
                         (if (> j-stars k-stars)
                           1
                           (if (< (:hn-d j) (:hn-id k)) -1 1))))
                -1)
              (if (pos? k-stars)
                1
                (if (< (:hn-d j) (:hn-id k)) -1 1)))]
      r)))

(def job-sorts [{:title "Creation" :key-fn :hn-id :order -1}
                ;{:title "Stars" :comp-fn job-stars-compare :order -1 :prep-fn job-stars-enrich}
                {:title "Company" :key-fn job-company-key :order 1}])

(defn sort-initialize []
  (reset! db/job-sort (nth job-sorts 0)))

;;; --- reusable components ------------------------------------------

(defn view-on-hn []
  (fn [attrs uri]
    [:a (merge {:href uri, :title "View on the HN site"} attrs)
     [:img {:src "dist/hn24.png"}]]))


(defn toggle-char [db-key title on-char off-char attrs style]
  (fn []
    (let [on-off (app-cursor db-key)]
      [:span (merge {:style    (merge {:font-weight "bold"
                                       :cursor      "pointer"
                                       :margin-left "9px"
                                       :font-family "Arial"
                                       :font-size   "1em"} style)
                     :title    title
                     :on-click #(swap! on-off not)}
               attrs)
       (unesc (if @on-off on-char off-char))])))

(defn slide-in-rule [in?]
  (if in? "slideIn" "slideOut"))

(defn open-shut-case [toggle-db-key title & cases]
  (fn []
    (let [open? (<app-cursor toggle-db-key)]
      (println :osc-open open?)
      [:div
       [:div.selector
        [:span title]
        [toggle-char toggle-db-key
         (str "Show/hide " title)
         "&#x25be", "&#x25b8" {} {}]]

       (into [:div {:class (str "osBody " (slide-in-rule open?))
                    :style {:background "#ff6600"
                            :padding    "6px"
                            :display    (if open? "block" "none")
                            }}]
         (for [case cases] (case)))])))