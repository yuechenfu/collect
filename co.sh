#!/bin/sh
./gradlew bootJar

scp build/libs/collect-0.0.1-SNAPSHOT.jar root@172.168.2.23:/root/sd/collect/collect.jar
ssh root@172.168.2.23 << remotessh
cd /root/sd/collect/
docker stop collect
docker run --privileged --rm -d -it --name collect --network mynetwork -p 9880:9880 -v /root/sd/collect:/collect -w /collect openjdk java -jar collect.jar
exit
remotessh


