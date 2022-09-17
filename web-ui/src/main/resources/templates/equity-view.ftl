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
    <script type="text/javascript" src="js/myjs.js"></script>
    <title>Home</title>
</head>
<body>
<#include "/fragments/header.ftl">
<div class="form-outline">
<input class="form-control" type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for script or type 'BUY' or 'SELL'" aria-label="Search">
</div>

<div class="scrollme">
<table id = "myTable" class="table table-bordered table-dark table-sm table-responsive">
    <thead>
    <th scope="col">Date</th>
    <th scope="col">Open</th>
    <th scope="col">Low</th>
    <th scope="col">High</th>
    <th scope="col">Close</th>
    <th scope="col">PrevClose</th>
    <th scope="col">Vol</th>
    </thead>
    <#list data as data>
        <tr>
            <td>${data.collectionDate}</a></td>
            <td>${data.open}</td>
            <td>${data.low}</td>
            <td>${data.high}</td>
            <td>${data.close}</td>
            <td>${data.prevClose}</td>
            <td>${data.vol}</td>
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