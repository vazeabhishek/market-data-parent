function myFunction() {
  // Declare variables
  var input, filter, table, tr, td, i, txtValue,lastCell;
  input = document.getElementById("myInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  lastCell = document.getElementById('myTable').rows[0].cells.length;
  console.log("lastCell is "+lastCell)
  isScript = checkIfScript(filter)

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    if(isScript)
    td = tr[i].getElementsByTagName("td")[0];
    else
    {
    td = tr[i].getElementsByTagName("td")[lastCell - 1];
     if(isBuy(filter))
      filter = "B";
     if(isSell(filter))
      filter = "S";
    }
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}
function filterLongShort() {
  // Declare variables
  var input, filter, table, tr, td, i, txtValue,lastCell;
  input = document.getElementById("myInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  lastCell = document.getElementById('myTable').rows[0].cells.length;
  isScript = checkIfLongShort(filter)

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    if(isScript)
    td = tr[i].getElementsByTagName("td")[0];
    else
    {
    td = tr[i].getElementsByTagName("td")[lastCell - 1];
     if(isLong(filter))
      filter = "L";
     if(isShort(filter))
      filter = "S";
    }
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}
function checkIfScript(filter){

 if (filter.toUpperCase().indexOf('BUY') > -1 || filter.toUpperCase().indexOf('SELL') > -1)
 {
   return false;
 }
 else
   return true;
 }

 function isBuy(filter){
 if (filter.toUpperCase().indexOf('BUY') > -1)
   return true;
 else
   return false;
 }
 function isSell(filter){
 if (filter.toUpperCase().indexOf('SELL') > -1)
   return true;
 else
   return false;
 }

 function checkIfLongShort(filter){

 if (filter.toUpperCase().indexOf('LONG') > -1 || filter.toUpperCase().indexOf('SHORT') > -1)
 {
   return false;
 }
 else
   return true;
 }

 function isLong(filter){
 if (filter.toUpperCase().indexOf('LONG') > -1)
   return true;
 else
   return false;
 }
 function isShort(filter){
 if (filter.toUpperCase().indexOf('SHORT') > -1)
   return true;
 else
   return false;
 }

$(document).ready(function() {

    $(".toggle-accordion").on("click", function() {
        var accordionId = $(this).attr("accordion-id"),
            numPanelOpen = $(accordionId + ' .collapse.in').length;

        $(this).toggleClass("active");

        if (numPanelOpen == 0) {
            openAllPanels(accordionId);
        } else {
            closeAllPanels(accordionId);
        }
    })

    openAllPanels = function(aId) {
        console.log("setAllPanelOpen");
        $(aId + ' .panel-collapse:not(".in")').collapse('show');
    }
    closeAllPanels = function(aId) {
        console.log("setAllPanelclose");
        $(aId + ' .panel-collapse.in').collapse('hide');
    }

});