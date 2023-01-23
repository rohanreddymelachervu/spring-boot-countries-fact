Spring Boot app that serves country details from API https://raw.githubusercontent.com/mledoze/countries/master/countries.json

Java JDK and JRE version: 17
Maven build
Server address: 127.0.0.1
Server port: 8081
(can be configured in application.properties)

Endpoins and methods:

(GET) /getJson:
returns JSON of the countries list 

(GET) /country//{name}
returns JSON of the country with name given

(GET) /filter/{region}
returns JSON of countries filtered by region

(GET) /sort
returns JSON of countries sorted in ascending order

(GET) /paginate?page="x"
returns "x" page of the JSON countries list

(GET) /property?country="c"?property="p"
returns property "p" present in country "c" if there. If "c" not specified, returns countries JSON list
