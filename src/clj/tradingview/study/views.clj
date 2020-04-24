(ns tradingview.study.views
  (:require
   [cheshire.core :refer [generate-string]]
   [json-html.core :refer [json->html]]
   ;[hiccup.core :refer :all]
   [hiccup.page :as page]
   [clj-time.coerce :as c]))

(defn to-date- [epoch-no-ms]
  (c/from-long (* epoch-no-ms 1000)))


(defn chart [c]
  [:tr
   [:td (to-date- (:timestamp c))]
   [:td (:id c)]
   [:td (:name c)]
   [:td (:symbol c)]
   [:td
    [:a.link {:href (str "/api/tvhack/json?id=" (:id c))} "json-download"]
    [:span "  "]
    [:a.link {:href (str "/hacked-chart-raw?id=" (:id c))} "json-visualized"]
    [:span "  "]
    [:a.link {:href (str "/hacked-chart-extract?id=" (:id c))} "download extracted data"]]])


(defn chart-list-page [tv]
  (let [list (.load-charts tv 77 77)]
    (page/html5
     [:head
      [:title "tradingview - hacked charts"]
      (page/include-css "json.human.css")]
     [:body
      [:h1 (str "hacked chart list " (count list))]
      [:p "json and json-raw-vizualised"]
      [:p "extract demo gets lines and shows start/end price/date"]
      [:table
       [:tr
        [:td "date"]
        [:td "id"]
        [:td "name"]
        [:td "symbol"]
        [:td]]
       (map chart list)]])))


(defn chart-json [tv id]
  (println "chart-json chart-id: " id)
  (let [chart (.load-chart tv 77 77 (Integer/parseInt id))
        json (:content chart)
        ;json-str (generate-string (:content chart))
        ;_ (println "json: " json-str)
        ]
    json))


(defn chart-raw-page [tv id]
  (println "chart-raw chart-id: " id)
  (let [chart (.load-chart tv 77 77 (Integer/parseInt id))
        json-str (generate-string (:content chart))
        _ (println "json: " json-str)]
    (page/html5
     [:head
      [:title "tradingview - hacked chsrt raw"]
      (page/include-css "json.human.css")]
     (json->html json-str))))

