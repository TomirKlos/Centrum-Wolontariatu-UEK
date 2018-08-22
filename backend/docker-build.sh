#!/usr/bin/env bash

sh  `pwd`/mvnw install
docker build -t centrum .
