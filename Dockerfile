FROM clojure:alpine-onbuild

CMD ["lein", "run", "/config.yml"]
