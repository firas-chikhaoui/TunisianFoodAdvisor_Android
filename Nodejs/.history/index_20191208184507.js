const mysql = require('mysql');
const express = require('express');
var app = express();
const bodyparser = require('body-parser');
var crypto = require('crypto');
var uuid = require('uuid');


var mysqlConnection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'projet_android',
});

mysqlConnection.connect((err) => {
    if (!err)
        console.log('DB connection succeded.');
    else
        console.log('DB connection failed \n Error : ' + JSON.stringify(err, undefined, 2));
});



// PASSWORD ULTIL
var genRandomString = function(length){
	return crypto.randomBytes(Math.ceil(length/2))
	.toString('hex') /* convert to hexa format*/
	.slice(0,length);/* return required number of characters*/
};


var sha512 =  function (password,salt){
	var hash = crypto.createHmac('sha512',salt); //Use SHA512
	hash.update(password);
	var value = hash.digest('hex');
	
	return {
		salt:salt,
		passwordHash:value
	};
	
	
};


function saltHashPassword (userPassword){
	var salt = genRandomString(16);
	var passwordData = sha512(userPassword,salt );
	return passwordData;
}
function checkHashPassword (userPassword,salt){
	var passwordData= sha512(userPassword, salt);
	return passwordData;
}


var app= express();
 app.use(bodyParser.json()); // Accept JSON Params 
 app.use(bodyParser.urlencoded({extended: true})); // Accept URL Encoded params 
 
 
 app.post('/register/',(req,res,next)=>{
	
    var post_data = req.body; //Get POST params
	var uid = uuid.v4();
	var plaint_password = post_data.password;
	
	var hash_data = saltHashPassword(plaint_password);
	var password = hash_data.passwordHash;
	
	var salt = hash_data.salt;
	var name = post_data.name;
	var email = post_data.email;
	
	con.query ('SELECT * FROM user where email=?' , [email],function (err,result,fields){
		con.on('error',function(err){
			console.log('[MySQL ERROR]',err);
		});

	if(result && result.length)
	res.json('user already exists');

	else{

	
	con.query('INSERT INTO `user`(`unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`) VALUES (?,?,?,?,?,NOW(),NOW())',[uid,name,email,password,salt], function(err,result,fields)
		{
			con.on('error',function(err){
				console.log('[MySQL ERROR]',err);
				res.json('Resgiter error: ', err);
			});
			res.json('Register successful');
		});
	}
	});

    
	
})

app.post('/login/',(req,res,next)=>{
	
    var post_data = req.body; //Get POST params
	
	var user_password = post_data.password;
	var email = post_data.email;

    con.query ('SELECT * FROM user where email=?' , [email],function (err,result,fields){
		con.on('error',function(err){
			console.log('[MySQL ERROR]',err);
		});


		if(result && result.length)
	{
		var salt = result[0].salt;
		var encrypted_password = result[0].encrypted_password;

		var hashed_password = checkHashPassword(user_password,salt).passwordHash;

		if (encrypted_password == hashed_password)
			res.end(JSON.stringify(result[0]))
			else
			res.end(JSON.stringify('Wrong password'));
	}
	

	else
	{
		res.json('user not exists');

	}

	
	});

    
	
	
})

//Get all getAllCat
app.get('/getAllCat/', (req, res) => {
    mysqlConnection.query('SELECT nom,image FROM categorie', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});

app.listen(4000, () => console.log('Express server is runnig at port no : 4000'));




