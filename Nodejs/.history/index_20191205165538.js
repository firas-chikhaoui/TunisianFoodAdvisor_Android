const mysql = require('mysql');
const express = require('express');
var app = express();
const bodyparser = require('body-parser');

app.use(bodyparser.json());

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


app.listen(4000, () => console.log('Express server is runnig at port no : 4000'));


//Get all getAllCat
app.get('/getAllCat', (req, res) => {
    mysqlConnection.query('SELECT nom,image FROM categorie', (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    })
});
