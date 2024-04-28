FROM ubuntu:latest
LABEL authors="yi"

ENTRYPOINT ["top", "-b"]