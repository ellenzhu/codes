var mongoose		=require('mongoose');
var schema	 		=mongoose.Schema;

//Data model for contacts using mongoose for mongodb

var contactSchema	= new schema(
{
	firstname:String,
	lastname:String,
	birthdate:String,
	address:String,
	phone:String,
	email:String
}
	);

//Exporting the module object as 'Contact' to be accessed globally
module.exports=mongoose.model('Contact',contactSchema);