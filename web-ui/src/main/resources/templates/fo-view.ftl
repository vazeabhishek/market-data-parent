<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="icon" href="../../favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="css/custom.css"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="js/myjs.js"></script>
    <title>Home</title>
</head>
<body>
<#include "/fragments/header.ftl">
<div class="container" style="width: 100%">
    <div class="accordion" id="accordion">
        <#list records as key,value>
            <div class="card">
                <div class="card-header" id="heading${key?index}">
                    <a class="card-link" data-toggle="collapse" href="#collapse${key?index}">
                        <span>${key}</span>  <span style="float: right">${value?size}</span>
                    </a>
                </div>
                <div id="collapse${key?index}" class="collapse hide" data-parent="#accordion">
                    <div class="card-body">
                        <table class="table table-light">
                            <thead>
                            <th scope="col">Symbol</th>
                            <th scope="col">Expiry</th>
                            <th scope="col">Buyers Won</th>
                            <th scope="col">Sellers Won</th>
                            <th scope="col">Higher High Count</th>
                            <th scope="col">Lower Low Count</th>
                            </thead>
                            <#list value as data>
                                <tr>
                                    <td><a href="/equity?symbol=${data.symbol}">${data.symbol}</a></td>
                                    <td>${data.expiryDate}</td>
                                    <td>${data.buyersWonCount}</td>
                                    <td>${data.sellersWonCount}</td>
                                    <td>${data.higherHighCount}</td>
                                    <td>${data.lowerLowCount}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
        crossorigin="anonymous"></script>
</body>
</html>