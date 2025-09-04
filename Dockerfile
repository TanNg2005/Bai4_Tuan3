FROM tomcat:11.0.10-jdk21

# Xoá app mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR từ dist/ (Ant build ra)
COPY dist/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]