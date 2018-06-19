(ns aghire.month-loader-views
  (:require
    [aghire.utility :refer [target-val] :as utl]

    [aghire.db :as db]
    [reagent.core :as r]
    [cljs.pprint :as pp]
    [aghire.month-loader :as loader]))

(def month-id (r/cursor db/app [:month-load :month-hn-id]))

(defn month-selector []
  (into [:select.searchMonth
         {:value     (or @month-id "")
          :on-change #(reset! month-id (utl/target-val %))}]
    (map #(let [{:keys [hnId desc]} %]
            [:option {:value hnId} desc])
      (loader/gMonthlies-cljs))))

(defn hn-month-link []
  ;; An HN icon <a> tag linking to the actual HN page.

  [utl/view-on-hn {}
   (pp/cl-format nil "https://news.ycombinator.com/item?id=~a" @month-id)])

;
;(def month-load (r/cursor db/app [:month-load]))
;(def month-phase (r/cursor month-load [:phase]))
;(def month-athings (r/cursor month-load [:phase]))
;(def month-phase (r/cursor month-load [:phase]))
;(def month-phase (r/cursor month-load [:phase]))
;(def month-jobs (r/cursor month-load [:jobs]))
;
;(defn month-load-fini [] (= :fini @month-phase))
;
;
;(defn month-jobs-total []
;  ;; A simple <span> announcing the job total once the load is complete
;  [:span {:style  {:color  "#fcfcfc"
;                   :margin "0 12px 0 12px"}
;          :hidden (not (month-load-fini))}
;   (str "Total jobs: " (count @month-jobs))])
;
;;;; -------------------------------------------------------------------
;;;; --- The star of the show ------------------------------------------
;;;; -------------------------------------------------------------------
;
;(fn [{:keys [phase page-url-count page-urls-remaining athings jobs]}]
;  (concat [phase]
;    (if (= :cull-athings phase)
;      [page-url-count (- page-url-count (count page-urls-remaining))]
;      [(count athings) (count jobs)])))
;
;(defn load-progress
;  ([] #(let [phase @month-phase]
;         (into [phase]
;           (case phase
;             :cull-athings [(count @month-url-pages) ()]))
;         (case phas)
;         :cull-athings (let [{:keys [page-url-count page-urls-remaining]}] ))
;  ([k v] (swap! person assoc-in k v)))
;
;(defn get-set-parent []
;  [:div
;   [:p "Current state: " (pr-str @person)]
;   [cursor-name-edit (r/cursor person-get-set [:name])]])

(defn month-load-progress-bar []

  (fn []
    (let [[phase max progress] @loader/month-progress]
      (println :phase phase)

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
   [:span @month-id]

   [:div {:style utl/hz-flex-wrap}
    [hn-month-link]
    ;[month-jobs-total]
    [month-load-progress-bar]
    ]])