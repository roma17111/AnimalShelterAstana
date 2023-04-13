#!/usr/bin/env tcsh
IMAGE_NAME=asha/default-image

pushd ../../
./mvn spring-boot:build-image -Dskip=true  -Dspring-boot.build-image.imaName=$IMAGE_NAME

docker image ls | grep $IMAGE_NAME
