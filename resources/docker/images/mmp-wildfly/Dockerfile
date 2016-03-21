# mmp-wildfly

FROM marcusportmann/mmp-java:1.0

MAINTAINER Marcus Portmann "http://mmp.guru"

# Install the WildFly JEE application server
RUN curl -o /tmp/wildfly-10.0.0.Final.tar.gz \
  http://download.jboss.org/wildfly/10.0.0.Final/wildfly-10.0.0.Final.tar.gz \
  && tar zxf /tmp/wildfly-10.0.0.Final.tar.gz -C /opt \
  && chown -R app.app /opt/wildfly-10.0.0.Final \
  && rm -f /opt/wildfly \
  && ln -s /opt/wildfly-10.0.0.Final /opt/wildfly \
  && rm -f /tmp/wildfly-10.0.0.Final.tar.gz


   



