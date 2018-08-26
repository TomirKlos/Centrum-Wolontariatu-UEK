#!/usr/bin/env bash

(
    cd ../frontend ;
    npm i ;
    npm run build
)

cp ../frontend/dist/* ./src/main/resources/static/

sh  `pwd`/mvnw install
docker build -t centrum .
