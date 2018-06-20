(ns aghire.filtering
  (:require

    [aghire.month-loader :as loader]
    [aghire.utility :as utl]
    [aghire.db :as db]
    [reagent.core :as r]))


;;; --- the filtering interface ------------------------------------------------------

(defn mk-job-selects [key lbl j-major-selects styling]
  (let [f-style (merge utl/hz-flex-wrap {:margin "8px 0 8px 24px"} styling)
        mk-job-select (fn [[tag desc]]
                        (let [f-active (r/cursor db/app [:filter-active tag])]
                          [:div {:style {:color       "white"
                                         :min-width   "96px"
                                         :display     "flex"
                                         :flex        ""
                                         :align-items "center"}}
                           [:input {:id           (str tag "ID")
                                    :class        (str tag "-jSelect")
                                    :style        {:background "#eee"}
                                    :type         "checkbox"
                                    :defaultValue false
                                    :on-change    (fn [e]
                                                    (reset! f-active (.-checked (.-target e))))}]
                           [:label {:for   (str tag "ID")
                                    :title desc}
                            tag]]))]
    (into [:div {:style f-style}]
      (map (fn [j-selects]
             (into [:div {:style {:display "flex"
                                  :flex    "no-wrap"}}]
               (map mk-job-select j-selects)))
        j-major-selects))))

(def title-selects [[["REMOTE", "Does regex search of title for remote jobs"]
                     ["ONSITE", "Does regex search of title for on-site jobs"]]
                    [["INTERNS", "Does regex search of title for internships"]
                     ["VISA", "Does regex search of title for Visa sponsors"]]])

(def user-selects [[["Starred", "Show only jobs you have rated with stars"]
                    ["Noted", "Show only jobs on which you have made a note"]]
                   [["Applied", "Show only jobs you have marked as applied to"]
                    ["Excluded", "Show jobs you exluded from view"]]])

(defn mk-title-selects []
  (mk-job-selects "title" "Title selects" title-selects {}))

(defn mk-user-selects []
  (mk-job-selects "user" "User selects" user-selects {}))

(defn jobs-filtered []
  @loader/month-jobs)