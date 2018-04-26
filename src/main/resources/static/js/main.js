$(document).ready(function(){

	$('.table .edit').on('click',function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		
		$.get(href,function(rights, status)){
			$('.editmodal #employeeid').val(rights.employeeID);
			$('.editmodal #email').val(rights.email);
			$('.editmodal #userrights').val(rights.user_rights);
		});
	
		$('.editmodal #exampleModal').modal();
		
		
	});
});


$(document).ready(function(){
	   $('#example').dataTable();
	});


$(document).ready(function() {
	var table = $('.display').dataTable();
	 
	$('table tbody').on( 'click', '.btn', function () {
		$(this).closest('tr').remove();
	} );
});