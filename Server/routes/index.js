var querystring = require('querystring');
var Fingerprint = require('../models/fingerprint');
var locateAlgorithms = require('./locateAlgorithms');
module.exports = function(app) {

  //Web Pages
  //--------------------------------------
  app.get('/testChoropleth', function(req, res) {
    res.render('testChoropleth', {
      title: 'testChoropleth page'
    });
  });

  app.get('/testLayers', function(req, res) {
    res.render('testLayers', {
      title: 'testLayers page'
    });
  });

  app.get('/testVampirefan', function(req, res) {
    res.render('testVampirefan', {
      title: 'testVampirefan page'
    });
  });

  app.get('/', function(req, res) {
    res.render('index', {
      title: 'SJTU Location Service Demo'
    });
  });

  app.get('/readDB', function(req, res) {
    var body = '';
    Fingerprint.getAll(function(error, fingerprints) {
      if(error) {
        body += 'finger failed. invaled postData.' + error.name + ': ' + error.message;
      }
      res.render('readDB', {
        title: 'SJTU Location Service Demo',
        subtitle: 'Data Base Information',
        dbdata: JSON.stringify(fingerprints)
      });
    });
  });

  app.get('/leaflet', function(req, res) {
    res.render('leaflet', {
      title: 'SJTU Location Service Demo'
    });
  });



  //Web Apps
  //---------------------------------------
  app.get('/start', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = 'You\'ve connected to SJTU Location Service Demo! Welcome! ^_^\n';
    body += '\'Finger\' for fingerpringting your current location;\n\'Locate\' for calculating your current location.\n';
    res.send(200, body);
  });
  app.post('/start', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = 'You\'ve connected to SJTU Location Service Demo! Welcome! ^_^\n';
    body += '\'Finger\' for fingerpringting your current location;\n\'Locate\' for calculating your current location.\n';
    res.send(200, body);
  });

  //method '/finger'
  app.get('/finger', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = 'You\'ve connected to SJTU Location Service Demo! Welcome! ^_^\n';
    body += 'Nice to see you trying to push a finger to our database!';
    res.send(200, body);
  });
  app.post('/finger', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = 'You\'ve tried to push a finger to the database.\n';

    var fingerprintInput = req.body;
    body += 'You\'ve sent the finger: ' + JSON.stringify(fingerprintInput) + '\n';
    // conso-le.log(fingerprintInput.locationId);
    if(fingerprintInput.locationId === undefined) {
      body += 'finger failed. locationId is null';
      res.send(200, body);
    } else {
      var newFingerprint = new Fingerprint({
        locationId: fingerprintInput.locationId,
        bearing: fingerprintInput.bearing,
        wapInfo: fingerprintInput.wapInfo
      });
      Fingerprint.getOne(fingerprintInput.locationId, function(error, fingerprint) {
        if(error) {
          body += 'finger failed. invaled postData.' + error.name + ': ' + error.message;
        }
        if(fingerprint === null) {
          newFingerprint.insert(function(error) {
            if(error) {
              body += 'finger failed. invaled postData.' + error.name + ': ' + error.message;
            }
          });
          body += 'finger complete.';
          console.log('mongodb has a new fingerprint:\n' + 'locationId=' + fingerprintInput.locationId);
        } else {
          console.log('finger failed. locationId exists.');
          body += 'finger failed. locationId exists.';
        }

        res.send(200, body);
      });
    }
  });

  //method '/locate'
  app.get('/locate', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = 'You\'ve connected to SJTU Location Service Demo! Welcome! ^_^\n';
    body += 'Nice to see you trying to locate your current position using our database!';
    res.send(200, body);
  });
  app.post('/locate', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = 'You\'ve tried to locate your current position using our database.\n';

    var locateFrame = req.body;
    body += 'You\'ve sent the locateFrame: ' + JSON.stringify(locateFrame) + '\n';
    locateAlgorithms.ed_advanced(body, locateFrame, function(error, body) {
      if(error) {
        body += 'locate failed. invaled postData.' + error.name + ': ' + error.message;
      }
      res.send(200, body);
    });
  });

  app.post('/Db.action', function(req, res) {
    res.set('Content-Type', 'text/plain');
    var body = '';

    switch(req.body.action) {
    case 'remove-all':
      Fingerprint.removeAll(function(error) {
        if(error) {
          body += 'fingerprints remove failed: ' + error.name + ': ' + error.message;
        }
        body += 'All fingerprints Removed';
        //respond
        res.send(200, body);
        res.redirect('/readDB');
      });
      break;

    case 'remove-by-locationId':
    // console.log(req.body.locationId);
      Fingerprint.removeByLocationId(req.body.locationId, function(error) {
        if(error) {
          body += 'fingerprint remove failed: ' + error.name + ': ' + error.message;
        }
        else body += 'fingerprint which locationId=' + req.body.locationId + ' is removed successfully';
        res.send(200, body);
        res.redirect('/readDB');
      });
      break;

    default:
      body += 'No action ' + req.body.action + ' found';
      res.send(200, body);
      res.redirect('/readDB');
    }
  });
};