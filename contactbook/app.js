//Require express framework,body-parser for json and initializing port
var express = require('express');
var bodyParser = require('body-parser');
var port = process.env.PORT || 8080; 
var app = express();

var Contact=require('./routes/contacts');


// Database connectivity to 'contactbook'
var mongoose   = require('mongoose');
mongoose.connect('mongodb://localhost:27017/contactbook'); // connect to our database



//configure app to use bodyParser() to get data from POST in json
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


// ROUTES FOR OUR CONTACT API
// =============================================================================

// get an instance of the express Router
var router = express.Router();              

router.use(function(req,res,next)
{
console.log("Processing request");
next();
});

// Handling GET at http://localhost:8080/api)
router.get('/', function(req, res) {
    res.json({ message: 'Welcome to contact book!' });   
});

// create a contact (accessed at POST http://localhost:8080/api/contacts)
router.route('/contacts')

    .post(function(req, res) {
        // create a new instance of the Contact model defined in contact.js
        var contact = new Contact();      
         // set the contact's details 
         contact.firstname = req.body.firstname; 
         contact.lastname=req.body.lastname;
         contact.address=req.body.address;
         contact.birthdate=req.body.birthdate;
         contact.phone=req.body.phone;
         contact.email=req.body.email;

        //persist the contact to the DB
        contact.save(function(err) {
            if (err)
                res.send(err);

            res.json({ message: 'Contact created!' });
        });
        
    })


// Display all contacts with  GET http://localhost:8080/api/contacts)
    .get(function(req, res) {
        Contact.find(function(err, contacts) {
            if (err)
                res.send(err);

            res.json(contacts);
        });
    });


//Retrieving particular contact by id using GET

router.route('/:contact_id')

    // get the contact with that id GET http://localhost:8080/api/contacts/:contact_id

    .get(function(req, res) {
        Contact.findById(req.params.contact_id, function(err, contacts) {
            if (err)
                res.send(err);
            res.json(contacts);
        });
    })


 //Updating a contact's details- Using PUT
    .put(function(req, res) {

        // use our bear model to find the bear we want
        Contact.findById(req.params.contact_id, function(err, contacts) {

            if (err)
                res.send(err);

            contacts.firstname = req.body.firstname;  // update the contact's info
            contacts.lastname = req.body.lastname;  
            contacts.phone = req.body.phone;  
            contacts.email = req.body.email; 
            contacts.bithdate=req.body.birthdate;
            contacts.address=req.body.address; 

            // save the contact
            contacts.save(function(err) {
                if (err)
                    res.send(err);

                res.json({ message: 'Contact updated!' });
            });

        });
    });


  //Searching for a particular contact using GET
router.route('/contacts/:contact_name')

    // get the contact with that id GET http://localhost:8080/api/contacts/:contact_name
//Performing case insensitive search using regular expression. All instances of names containing 'contact_name' will be retrieved
    .get(function(req, res) {   
       Contact.find({firstname: new RegExp(req.params.contact_name, "i")},function(err, contacts) {
            if (err)
                res.send(err);
            res.json(contacts);
        });
    });


// Deleting a contact -Using DELETE

router.route('/contacts/:contact_id').delete(function(req, res) {
        Contact.remove({
            _id: req.params.contact_id
        }, function(err, contacts) {
            if (err)
                res.send(err);
            res.json({ message: 'Successfully deleted the contact' });
         

        });
    });

// REGISTER OUR ROUTES -------------------------------
// all of our routes will be prefixed with /api
app.use('/api', router);


// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Listening on port ' + port);

module.exports = app;
