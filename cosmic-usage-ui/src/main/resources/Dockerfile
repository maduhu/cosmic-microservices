FROM node:alpine

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app

RUN sleep 5 && apk update && apk upgrade && \
    apk add --no-cache bash git && \
    npm install --global bower http-server && \
    bower install --allow-root

EXPOSE 8080

CMD [ "http-server", "/usr/src/app" ]
