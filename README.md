# jsf-spring-boot   [![Build Status](https://travis-ci.org/hwolf/jsf-spring-boot.svg?branch=master)](https://travis-ci.org/hwolf/jsf-spring-boot)
A Spring Boot starter for JSF 2.2

Spring Boot doesn't have a starter for JSF. This project changes this. In the Internet you find several blogs how JSF and Spring Boot can be integrated, e.g. [JSF 2.2 and PrimeFaces 5 on Spring Boot](http://www.beyondjava.net/blog/jsf-2-2-primefaces-5-spring-boot/), [Spring Boot with JSF/Primefaces](http://www.oakdalesoft.com/2015/09/spring-boot-with-jsfprimefaces/). But these solutions doesn't work fully due to a change in Mojarra 2.2.10/11.

*Idea: Implement the missing part in Spring Boot*

*Other problem: JSF doesn't find Facelets in the webapp folder when running the application as executable JAR*

Tested with
- Spring Boot 1.3.1
- JSF 2.2 (Mojarra 2.2.12)
- Undertow 1.3.12.Final
- Java 7

Showcase based on Primefaces Showcase 5.3
