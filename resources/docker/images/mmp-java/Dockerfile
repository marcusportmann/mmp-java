# mmp-java:1.0

FROM alpine:3.3

MAINTAINER Marcus Portmann "http://mmp.guru"

# Install dependencies 
RUN apk --update add curl ca-certificates tar sqlite icu bash && \
    curl -Ls https://circle-artifacts.com/gh/andyshinn/alpine-pkg-glibc/6/artifacts/0/home/ubuntu/alpine-pkg-glibc/packages/x86_64/glibc-2.21-r2.apk > /tmp/glibc-2.21-r2.apk && \
    apk add --allow-untrusted /tmp/glibc-2.21-r2.apk

# Java version
ENV JAVA_VERSION_MAJOR 8 
ENV JAVA_VERSION_MINOR 74
ENV JAVA_VERSION_BUILD 02 
ENV JAVA_PACKAGE       jdk

# Download and unarchive Java
RUN mkdir /opt && curl -jksSLH "Cookie: oraclelicense=accept-securebackup-cookie"\
  http://download.oracle.com/otn-pub/java/jdk/8u92-b14/server-jre-8u92-linux-x64.tar.gz \
    | tar -xzf - -C /opt &&\
    ln -s /opt/jdk1.8.0_77 /opt/jdk &&\
    rm -rf /opt/jdk/*src.zip \
           /opt/jdk/lib/missioncontrol \
           /opt/jdk/lib/visualvm \
           /opt/jdk/lib/*javafx* \
           /opt/jdk/jre/lib/plugin.jar \
           /opt/jdk/jre/lib/ext/jfxrt.jar \
           /opt/jdk/jre/bin/javaws \
           /opt/jdk/jre/lib/javaws.jar \
           /opt/jdk/jre/lib/desktop \
           /opt/jdk/jre/plugin \
           /opt/jdk/jre/lib/deploy* \
           /opt/jdk/jre/lib/*javafx* \
           /opt/jdk/jre/lib/*jfx* \
           /opt/jdk/jre/lib/amd64/libdecora_sse.so \
           /opt/jdk/jre/lib/amd64/libprism_*.so \
           /opt/jdk/jre/lib/amd64/libfxplugins.so \
           /opt/jdk/jre/lib/amd64/libglass.so \
           /opt/jdk/jre/lib/amd64/libgstreamer-lite.so \
           /opt/jdk/jre/lib/amd64/libjavafx*.so \
           /opt/jdk/jre/lib/amd64/libjfx*.so \
    && addgroup -g 999 app && adduser -D  -G app -s /bin/false -u 999 app \
    && rm -rf /tmp/* \
    && rm -rf /var/cache/apk/* \
    && echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf

# Set environment
ENV JAVA_HOME /opt/jdk
ENV PATH ${PATH}:/opt/jdk/bin	

# Install the unrestricted Java policy files
RUN curl -jksSLH "Cookie: oraclelicense=accept-securebackup-cookie" \
  -o /tmp/jce_policy-8.zip http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip \
  && unzip -o /tmp/jce_policy-8.zip -d /tmp \
  && rm -f /opt/jdk/jre/lib/security/local_policy.jar \
  && rm -f /opt/jdk/jre/lib/security/US_export_policy.jar \
  && cp /tmp/UnlimitedJCEPolicyJDK8/local_policy.jar /opt/jdk/jre/lib/security/ \
  && cp /tmp/UnlimitedJCEPolicyJDK8/US_export_policy.jar /opt/jdk/jre/lib/security/ \
  && rm -rf /tmp/UnlimitedJCEPolicyJDK8 \
  && rm -rf /tmp/jce_policy-8.zip

