#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/officefinder
cd $REPOSITORY

APP_NAME=officefinder
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할 application 이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &