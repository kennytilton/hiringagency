(ns aghire.job-listing-control-bar
  (:require [reagent.core :as r]
            [aghire.utility :refer [target-val] :as utl]
            [aghire.db :as db]
            [aghire.month-loader :as loader]))

;;; --- sub-components ---------------------------------------

(defn job-expansion-control []
  (let [toggle-all-details-expands (r/atom true)]
    (fn []
      [:button {:style    {:font-size "1em"
                           :min-width "128px"}
                :on-click #(let [expand? @toggle-all-details-expands
                                 jobs @loader/month-jobs]
                             (println :setting-all-deetshow expand? (count jobs))
                             (swap! toggle-all-details-expands not)
                             (let [newdeets (into {} (for [j jobs]
                                                  [(:hn-id j) expand?]))]
                               (println :newdeets newdeets)
                               (reset! db/show-job-details
                                 newdeets)))}
       (if @toggle-all-details-expands
         "Expand all" "Collapse all")])))


#_(defn excluded-count []
    (fn []
      (let [excluded-ct (<sub [:jobs-filtered-excluded-ct])]
        [:span {:style    {:padding-bottom "4px"
                           :cursor         "pointer"
                           :display        "flex"
                           :align-items    "center"
                           :font-size      "1em"
                           :visibility     (if (pos? excluded-ct) "visible" "hidden")
                           :border         (if (<sub [:show-filtered-excluded])
                                             "thin solid red" "none")
                           :title          "Show/hide items you have excluded"}
                :on-click #(>evt [:show-filtered-excluded-toggle])
                }
         (str (utl/unesc "&#x20E0;") ": " excluded-ct)])))

(defn result-max []
    (fn []
      [:div {:style (merge utl/hz-flex-wrap-centered {:margin-right "6px"})}
       [:span "Show:"]
       (let [rmax @db/job-display-max
             set-new-max (fn [event]
                           (reset! db/job-display-max (js/parseInt (target-val event))))]
         [:input {:type         "number"
                  :defaultValue rmax

                  :on-key-press #(when (= "Enter" (js->clj (.-key %)))
                                   (set-new-max %))

                  :on-blur      #(set-new-max %)

                  :style        {:font-size    "1em"
                                 :max-width    "48px"
                                 :margin-left  "6px"
                                 :margin-right "6px"
                                 }}])]))

;;; --- the beef --------------------------------------------------

(defn job-listing-control-bar []
  (fn []
    [:div.listingControlBar
     #_[:div {:style utl/hz-flex-wrap-centered}
        ;;; --- match count---------------------------------------------------
        [:span {:style {:font-size    "1em"
                        :margin-right "12px"}}
         (let [jobs (<sub [:jobs-filtered])]
           (str "Jobs: " (count jobs)))]

        [excluded-count]]
     [result-max]
     [job-expansion-control]]))

;;; --- reframe plumbing ------------------------------------------------
;

;(rfr/reg-sub :jobs-filtered-excluded-ct
;  ;
;  ; This is tricky. jobs-filtered includes excluded jobs (!) so we can let
;  ; the user show/hide them while browsing a selection. Kinda like when
;  ; we search gmail and it says "10 found, 3 more in trash". Sooo...
;  ;
;  ; This subscription provides the info needed to inform users
;  ; how many jobs matched by their search are hidden
;  ; because the user has excluded them.
;  ;
;  ;; signal fn
;  (fn [query-v _]
;    [(subscribe [:jobs-filtered])
;     (subscribe [:user-notes])])

;; compute
;(fn [[jobs-filtered user-notes]]
;  #_(println :jfilex-sees (count jobs-filtered) (count user-notes))
;  (count (filter (fn [j]
;                   (get-in user-notes [(:hn-id j) :excluded]))
;           jobs-filtered))) )
