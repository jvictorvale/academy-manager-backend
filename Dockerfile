FROM ubuntu:latest
LABEL authors="Victor Vale"

ENTRYPOINT ["top", "-b"]