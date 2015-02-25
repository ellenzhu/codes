/**
 * Created by alabrazi on 24/01/15.
 */
  $(document).ready(function() {
    'use strict';
    $(window).on('message', function(evt) {
      //Note that messages from all origins are accepted

      //Get data from sent message
      var data = evt.originalEvent.data;
      //Create a new list item based on the data
      var newItem = '\n\t<li>' + (data.payload || '') + '</li>';
      //Add the item to the beginning of the actions list
      $('#actions').prepend(newItem);
    });
  });