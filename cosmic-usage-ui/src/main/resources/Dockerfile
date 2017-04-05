FROM node:alpine

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . public views /usr/src/app/

RUN sleep 5 && apk update && apk upgrade && \
    apk add --no-cache bash git && \
    npm install && \
    npm install --global bower && \
    bower install --allow-root

EXPOSE 8080

CMD npm start
