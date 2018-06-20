(ns aghire.job-list
  (:require
    [reagent.core :as r]
    [aghire.db :as db]
    ;[aghire.events :as evt]
    [aghire.filtering :as flt]
    ;[aghire.user-annotations :as unt]
    [goog.string :as gs]
    [cljs.pprint :as pp]
    [aghire.utility :refer [target-val] :as utl]
    [aghire.month-loader :as loader]))

(declare job-header job-details)

(defn job-list-sort [jobs]
  (let [{:keys [key-fn comp-fn order prep-fn]} @db/job-sort]
      (sort (fn [j k]
              (if comp-fn
                (comp-fn order j k)
                (* order (if (< (key-fn j) (key-fn k)) -1 1))))
        (map (or prep-fn identity) jobs))))

(defn jump-to-hn [hn-id]
  (.open js/window (pp/cl-format nil "https://news.ycombinator.com/item?id=~a" hn-id) "_blank"))

(defn job-list-item []
  (fn [job-no job]
    [:li {:style {:cursor     "pointer"
                  :display    "block" #_(let [excluded (<sub [:unotes-prop (:hn-id job) :excluded])]
                                          (if (and excluded
                                                   (not (<sub [:show-filtered-excluded]))
                                                   (not (<sub [:filter-active "Excluded"])))
                                            "none" "block"))
                  :padding    "12px"
                  :background (if (zero? (mod job-no 2))
                                "#eee" "#f8f8f8")}}
     [job-header job]
     [job-details job]
     ]))

(defn job-list []
  (fn []
    (when (loader/month-load-fini)
      (into [:ul {:style {:list-style-type "none"
                          :background      "#eee"
                          ;; these next defeat gratuitous default styling of ULs by browser
                          :padding         0
                          :margin          0}}]
        (map (fn [jn j]
               ^{:key (:hn-id j)} [job-list-item jn j])
          (range) ;; provides zebra
          (job-list-sort
            (take @db/job-display-max @flt/jobs-filtered)))))))

(defn job-details []
  (fn [job]
    (let [deets @(r/cursor db/app [:show-job-details (:hn-id job)])]
      [:div {:class (if deets "slideIn" "slideOut")
             :style {:margin     "6px"
                     :background "#fff"
                     :display    (if deets "block" "none")}}
       #_[unt/user-annotations job]
       (into [:div {:style           {:margin   "6px"
                                      :overflow "auto"}
                    :on-double-click #(jump-to-hn (:hn-id job))}]
         (when deets
           (map (fn [node]
                  (case (.-nodeType node)
                    1 [:p (.-innerHTML node)]
                    3 [:p (.-textContent node)]
                    [:p (str "Unexpected node type = " (.-nodeType node))]))
             (:body job))))])))

(defn job-header []
  (fn [job]
    [:div {:style {:cursor  "pointer"
                   :display "flex"}
           ;; :on-click #(>evt [::evt/toggle-show-job-details (:hn-id job)])
           }
     [:span {:style {:color        "gray"
                     :max-height   "16px"
                     :margin-right "9px"
                     :display      "block"}}
      (utl/unesc "&#x2b51")]
     [:span (:title-search job)]]))


