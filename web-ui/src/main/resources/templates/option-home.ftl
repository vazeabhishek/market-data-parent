<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="css/custom.css"/>
    <title>Home</title>
</head>
<body>
<#include "/fragments/header.ftl">
<div class="scrollme">
<table class="table table-bordered table-dark table-sm table-responsive">
        <#list optionRows as rkey, rValue>
         <#assign trendClass = "no-trend-row">
         <#assign i = rValue?size>
         <#if rValue[i-1]??>
                     <#if rValue[i-1].cumulativeTrend &gt; 1>
                         <#assign trendClass = "positive-trend-row">
                      </#if>
                      <#if rValue[i-1].cumulativeTrend &lt; -1>
                         <#assign trendClass = "negetive-trend-row">
                     </#if>
           </#if>
          <tr class=${trendClass} scope="row">
             <td class="nowrap"><a class="link" href = "/option/${rValue[0].optionRecordNo}">${rkey}</a></td>
              <#list rValue as data>
             <#if data.signal??>
              <#if data.signal?matches("B")>
                 <td bgcolor="green" title= "${data.ltp}"> ${data.signal}</td>
              <#elseif  data.signal?matches("S")>
                <td bgcolor="red" title= "${data.ltp}"> ${data.signal}</td>
              <#elseif  data.signal?matches("W")>
                <td bgcolor="orange" title= "${data.ltp}"> ${data.signal}</td>
               </#if>
              </#if>

              <#else>
              <td>-</td>
              </#list>
          </tr>
        </#list>
   </table>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>
</body>
</html>