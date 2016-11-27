FROM java:openjdk-8-jdk

ENV         ACTIVATOR_VERSION 1.3.11
ARG         USER_HOME_DIR="/root"

# Add cerificates that ensure download of dependencies works:
RUN apt-get install -y ca-certificates-java

# Install Typesafe Activator
RUN         cd /tmp && \
            wget -q http://downloads.typesafe.com/typesafe-activator/$ACTIVATOR_VERSION/typesafe-activator-$ACTIVATOR_VERSION.zip && \
            unzip -q typesafe-activator-$ACTIVATOR_VERSION.zip -d /usr/local
RUN         mv /usr/local/activator-dist-$ACTIVATOR_VERSION /usr/local/activator && \
            rm typesafe-activator-$ACTIVATOR_VERSION.zip

COPY . /w3act

WORKDIR /w3act

RUN /usr/local/activator/bin/activator -Dconfig.file=/w3act/conf/dev.conf stage

EXPOSE 9000

#VOLUME "$USER_HOME_DIR/.ivy2"

CMD /w3act/target/universal/stage/bin/w3act -Dconfig.file=/w3act/conf/dev.conf -Dpidfile.path=/dev/null

