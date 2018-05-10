(defproject movie-list "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.0"]
                 [re-frame "0.10.5"]
                 [cljs-react-material-ui "0.2.48"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :nrepl-port 7002
             :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
         {:dependencies [[binaryage/devtools "0.9.10"]
                         [day8.re-frame/re-frame-10x "0.3.3"]
                         [day8.re-frame/tracing "0.5.1"]
                         [com.cemerick/piggieback "0.2.2"]
                         [figwheel-sidecar "0.5.5"]]

          :plugins      [[lein-figwheel "0.5.16"]
                         [lein-doo "0.1.10"]]
          :source-paths ["env/dev/clj"]}
   :prod {:dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "env/dev/cljs"]
     :figwheel     {:on-jsload "movie-list.core/mount-root"}
     :compiler     {:main                 movie-list.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            movie-list.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          movie-list.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}
    ]}

  )
