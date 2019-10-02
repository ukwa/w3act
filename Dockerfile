FROM openjdk:8 AS build-env

ENV         ACTIVATOR_VERSION 1.3.11
ARG         USER_HOME_DIR="/root"

# Install Typesafe Activator
RUN         cd /tmp && \
            wget -q http://downloads.typesafe.com/typesafe-activator/$ACTIVATOR_VERSION/typesafe-activator-$ACTIVATOR_VERSION.zip && \
            unzip -q typesafe-activator-$ACTIVATOR_VERSION.zip -d /usr/local
RUN         mv /usr/local/activator-dist-$ACTIVATOR_VERSION /usr/local/activator && \
            rm /tmp/typesafe-activator-$ACTIVATOR_VERSION.zip

COPY . /w3act
COPY .git /w3act/.git

WORKDIR /w3act

# Patch in the version tag:
RUN git fetch --tags --force --no-recurse-submodules https://github.com/ukwa/w3act.git && export VERSION=`git describe --tags --always` && sed -i -r 's|version := ".*"|version := "'${VERSION}'"|' build.sbt

# Run without failing to try to download all dependencies:
RUN /usr/local/activator/bin/activator stage || exit 0
RUN /usr/local/activator/bin/activator stage || exit 0

# Perform a full clean build:
RUN rm -fr target
RUN /usr/local/activator/bin/activator clean stage

# And patch onto a smaller image:
FROM openjdk:8-jre

COPY --from=build-env /w3act/target/universal/stage /w3act

EXPOSE 9000

# Have to use this as the working directory or it fails to find the email templates!
WORKDIR /w3act

# Install GeoIP:
RUN curl -L -O http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz && gunzip GeoLite2-City.mmdb.gz

# Use larger heap, and add experimental option: forcing restart on OOM:
CMD /w3act/bin/w3act -J-Xmx4g -J-XX:+ExitOnOutOfMemoryError -Dconfig.file=/w3act/conf/docker.conf -Dpidfile.path=/dev/null
