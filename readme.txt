This project is a Java web app to invoke the Radian6 REST api
Authentication is required

e.g. localhost:8080/radian6/logon?username=abc&password=xyz
e.g. localhost:8080/radian6/timeseries?startdate=1318906800000&enddate=1320030000000&topics=296954&mediatypes=8,12&segmentation=2

to build:

mvn clean compile

to run:

mvn jetty:run


git add *
git commit -m "comment"
git push -u origin master