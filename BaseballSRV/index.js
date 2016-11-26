var express    = require('express');
var mysql      = require('mysql');
var bodyParser = require('body-parser');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'qwer58266!',
  port     : '3306',
  database : 'baseball'
}); // database

var app = express();

app.set('port', process.env.PORT || 8080);
app.use(bodyParser.urlencoded({ extended: true })); 
app.use(bodyParser.text()); 
app.use(bodyParser.json());

app.post('/join', function(req, res) {
  console.log('tt');
  var id = req.body.id;
  var pw = req.body.pw;
  var name = req.body.name;
  var age = req.body.age;

  console.log('userId: ' + id + ', userPw: ' + pw + ', userName: ' + name + ', userAge: ' + age);
  var data = [id, pw, name, age];
  connection.query('insert into USERS values(?, ?, ?, ?)', data, function (error, result) {
  	if (error) {
      console.log(error);
      res.send( { success: 0 } );
  	} else {
      res.send( { success: 1 } );
    }
  });
});

app.post('/idCheck', function (req, res) {
  var id = req.body.id;
  var data = [id];

  connection.query('select * from USERS where ID = ?', data, function (error, result) {
    if (error) { console.log(error); }
    console.log(result);
    if (result.length == 0) {
      res.send( {success: 1 } );
    } else {
      res.send( {success: 0 } );
    }
  });
})

app.post('/login', function (req, res) {
  var id = req.body.id;
  var pw = req.body.pw;
  var data = [id, pw];

  connection.query('select * from USERS where ID = ? and PW = ?', data, function (error, result) {
    console.log(result.length);

    if (result.length == 1) {
      res.send( {success: 1, name: result[0].NAME } );
    } else {
      res.send( {success: 0 } );
    }
  });
})

app.post('/setupCam', function (req, res) {
 var id = req.body.id;
 var def = req.body.def;
 var data = [id, def];

 connection.query('insert into CAMERAS values(?, ?)', data, function (error, result) {
 	if (error) {
		res.send( { success: 0 } );
	} else {
		res.send( { success: 1 } );
	}
 });
});

app.post('/deleteCam', function (req, res) {
 var id = req.body.id;
 var data = [id];

 connection.query('delete from CAMERAS where ID = ?', data, function (error, result) {
 	if (error) {
		res.send( { success: 0 } );
	} else {
		res.send( { success: 1 } );
	}
 });
});

app.post('/getCamList', function (req, res) {
  var resultArray = new Array();
  connection.query('select * from CAMERAS', function (error, result) {
		res.send( {list: result} );
  });

})
app.listen(app.get('port'), function () {
  console.log('Express server listening on port ' + app.get('port'));
});
