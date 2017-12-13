(ns fhir-diff.views.helpers)

(defn column-list [el name body]
  [:div {:class "col-md-4"}
   [:h3 name]
   [el {:class "list-group"} body]])

(defn column-list-items [name items class]
  [column-list :ul name
   (for [item items] ^{:key item}
     [:li {:class (str "list-group-item " class)}
      item])])
