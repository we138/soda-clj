(defproject soda-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [integrant "0.8.0"]
                 [metosin/reitit "0.5.17"]
                 [ring/ring-jetty-adapter "1.9.3"]
                 [metosin/muuntaja "0.6.8"]
                 [metosin/malli "0.8.4"]
                 [com.github.seancorfield/next.jdbc "1.2.772"]
                 [org.postgresql/postgresql "42.3.3"]
                 [clj-http "3.12.3"]
                 [clj-time "0.15.2"]
                 [ragtime "0.8.0"]
                 [cambium/cambium.core "1.1.1"]
                 [cambium/cambium.codec-simple "1.0.0"]
                 [cambium/cambium.logback.core "0.4.5"]]
  :plugins [[lein-cljfmt "0.8.0"]]
  :main ^:skip-aot soda-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :aliases {"migrate" ["run" "-m" "soda-clj.system/run-migrations"]
            "server" ["run" "-m" "soda-clj.system/-main"]})

