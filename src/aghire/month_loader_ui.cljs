(ns aghire.month-loader-ui
  (:require
    [aghire.utility :refer [target-val] :as utl]

    [aghire.db :as db]
    [reagent.core :as r]
    [cljs.pprint :as pp]
    [aghire.month-loader :as loader]))

(defn month-selector []
  (into [:select.searchMonth
         {:value     (or @loader/month-id "")
          :on-change #(reset! loader/month-id (utl/target-val %))}]
    (map #(let [{:keys [hnId desc]} %]
            [:option {:value hnId} desc])
      (loader/gMonthlies-cljs))))

(defn hn-month-link []
  ;; An HN icon <a> tag linking to the actual HN page.
  [utl/view-on-hn {}
   (pp/cl-format nil "https://news.ycombinator.com/item?id=~a" @loader/month-id)])

(defn month-jobs-total []
  ;; A simple <span> announcing the job total once the load is complete
  [:span {:style  {:color  "#fcfcfc"
                   :margin "0 12px 0 12px"}
          :hidden (not (loader/month-load-fini))}
   (str "Total jobs: " (count @loader/month-jobs))])

;;;; -------------------------------------------------------------------
;;;; --- The star of the show ------------------------------------------
;;;; -------------------------------------------------------------------
;

(defn month-load-progress-bar []

  (fn []
    (let [;; load @loader/athings-to-jobs
          [phase max progress] @loader/month-progress]

      [:div {:hidden (= phase :fini)}
       [:span
        (case phase
          :cull-athings "Scrape nodes "
          :parse-jobs "Parse jobs "
          "")]
       [:progress
        {:value progress
         :max   max}]])))

(defn pick-a-month []
  [:div.pickAMonth
   [month-selector]

   [:div {:style utl/hz-flex-wrap}
    [hn-month-link]
    [month-jobs-total]
    [month-load-progress-bar]
    ]])